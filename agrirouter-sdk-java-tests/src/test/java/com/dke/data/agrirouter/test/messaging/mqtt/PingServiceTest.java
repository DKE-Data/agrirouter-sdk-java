package com.dke.data.agrirouter.test.messaging.mqtt;

import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.messaging.mqtt.PingService;
import com.dke.data.agrirouter.api.service.parameters.PingParameters;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttClientService;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttOptionService;
import com.dke.data.agrirouter.impl.messaging.mqtt.PingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PingServiceTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingServiceTest.class);

    private static boolean messageHasArrived = false;
    private static boolean messageHasBeenDelivered = false;


    @Test
    void sendHealthMessageToPingEndpoint() throws Throwable {
        var onboardingResponse = OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.MQTT_COMMUNICATION_UNIT);

        var mqttClientService = new MqttClientService(new QA() {
        });
        var mqttClient = mqttClientService.create(onboardingResponse);

        var mqttOptionService = new MqttOptionService(new QA() {
        });
        mqttClient.setCallback(new PingServiceTest.InternalCallback());
        mqttClient.connect(mqttOptionService.createMqttConnectOptions(onboardingResponse));
        mqttClient.subscribe(onboardingResponse.getConnectionCriteria().getCommands());

        var pingParameters = new PingParameters();
        pingParameters.setOnboardingResponse(onboardingResponse);

        PingService pingService = new PingServiceImpl(mqttClient);
        pingService.send(pingParameters);

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

    static class InternalCallback implements MqttCallback {


        @Override
        public void connectionLost(Throwable throwable) {
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            messageHasArrived = true;
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            messageHasBeenDelivered = true;
        }
    }


}
