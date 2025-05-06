package com.dke.data.agrirouter.test.messaging.mqtt;

import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.mqtt.PingService;
import com.dke.data.agrirouter.api.service.parameters.PingParameters;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttClientService;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttOptionService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.mqtt.PingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

class PingServiceTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingServiceTest.class);

    private boolean messageHasArrived = false;
    private boolean messageHasBeenDelivered = false;
    private static String healthMessageId = null;

    private final DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();

    @Test
    void sendHealthMessageToPingEndpoint() throws Throwable {
        var onboardingResponse = OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.MQTT_COMMUNICATION_UNIT);

        var mqttClientService = new MqttClientService(communicationUnit.getEnvironment());
        var mqttClient = mqttClientService.create(onboardingResponse);

        var mqttOptionService = new MqttOptionService(communicationUnit.getEnvironment());
        mqttClient.setCallback(new PingServiceTest.InternalCallback());
        mqttClient.connect(mqttOptionService.createMqttConnectOptions(onboardingResponse));
        mqttClient.subscribe(onboardingResponse.getConnectionCriteria().getCommands());

        var pingParameters = new PingParameters();
        pingParameters.setOnboardingResponse(onboardingResponse);

        PingService pingService = new PingServiceImpl(mqttClient);
        healthMessageId = pingService.send(pingParameters);

        var nrOfRetries = 0;
        while (!messageHasBeenDelivered && !messageHasArrived && nrOfRetries < 10) {
            LOGGER.debug("Waiting for message to arrive, retrying in 1 second. This is the {} retry.", nrOfRetries);
            //noinspection BusyWait
            Thread.sleep(1000);
            nrOfRetries++;
        }
        mqttClient.disconnect();

        Assertions.assertTrue(messageHasBeenDelivered, "Message has not delivered within the timeout configured. There were " + nrOfRetries + " retries.");
        Assertions.assertTrue(messageHasArrived, "Message has not arrived within the timeout configured. There were " + nrOfRetries + " retries.");
    }

    class InternalCallback implements MqttCallback {


        @Override
        public void connectionLost(Throwable throwable) {
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            var payload = StringUtils.toEncodedString(mqttMessage.getPayload(), StandardCharsets.UTF_8);
            final var fetchMessageResponse = new Gson().fromJson(payload, FetchMessageResponse.class);
            final var decodedMessageResponse = decodeMessageService.decode(fetchMessageResponse.getCommand().getMessage());
            LOGGER.debug("Message arrived: {}", decodedMessageResponse);
            if (decodedMessageResponse.getResponseEnvelope().getType() == Response.ResponseEnvelope.ResponseBodyType.ACK) {
                messageHasArrived = StringUtils.equals(decodedMessageResponse.getResponseEnvelope().getApplicationMessageId(), healthMessageId);
            } else {
                LOGGER.warn("Message arrived, but this should not be the case {}.", decodedMessageResponse);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            messageHasBeenDelivered = true;
        }
    }


}
