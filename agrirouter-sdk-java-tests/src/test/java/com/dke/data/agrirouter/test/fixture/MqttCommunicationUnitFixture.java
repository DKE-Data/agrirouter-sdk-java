package com.dke.data.agrirouter.test.fixture;

import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.ApplicationType;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttClientService;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttOptionService;
import com.dke.data.agrirouter.impl.messaging.mqtt.SetCapabilityServiceImpl;
import com.dke.data.agrirouter.impl.onboard.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class to onboard endpoints for different reasons.
 */
@SuppressWarnings("ALL")
class MqttCommunicationUnitFixture extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttCommunicationUnitFixture.class);

    private static boolean messageHasArrived = false;
    private static boolean messageHasBeenDelivered = false;

    /**
     * Create a new registration token by using the agrirouter UI and select the integration test
     * application for CUs.
     */
    @Test
    //@Disabled("Please replace the placeholder for the registration code to run the test case.")
    void onboardCommunicationUnitAndSaveToFile() throws Throwable {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        OnboardingParameters onboardingParameters = new OnboardingParameters();
        onboardingParameters.setRegistrationCode("4822479417");
        onboardingParameters.setApplicationId(communicationUnit.getApplicationId());
        onboardingParameters.setCertificationVersionId(communicationUnit.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.MQTT.getKey());
        onboardingParameters.setApplicationType(ApplicationType.APPLICATION);
        onboardingParameters.setUuid(UUID.randomUUID().toString());
        final OnboardingResponse onboardingResponse = onboardingService.onboard(onboardingParameters);
        assertNotNull(onboardingResponse);
        assertNotNull(onboardingResponse.getCapabilityAlternateId());
        assertNotNull(onboardingResponse.getDeviceAlternateId());
        assertNotNull(onboardingResponse.getSensorAlternateId());
        assertNotNull(onboardingResponse.getAuthentication());
        assertNotNull(onboardingResponse.getAuthentication().getCertificate());
        assertNotNull(onboardingResponse.getAuthentication().getSecret());
        assertNotNull(onboardingResponse.getAuthentication().getType());
        assertNotNull(onboardingResponse.getConnectionCriteria());
        assertNotNull(onboardingResponse.getConnectionCriteria().getMeasures());
        assertNotNull(onboardingResponse.getConnectionCriteria().getCommands());
        OnboardingResponseRepository.save(
                OnboardingResponseRepository.Identifier.MQTT_COMMUNICATION_UNIT, onboardingResponse);

        MqttClientService mqttClientService = new MqttClientService(new QA() {
        });
        IMqttClient mqttClient = mqttClientService.create(onboardingResponse);

        MqttOptionService mqttOptionService = new MqttOptionService(new QA() {
        });
        mqttClient.setCallback(new InternalCallback());
        mqttClient.connect(mqttOptionService.createMqttConnectOptions(onboardingResponse));
        mqttClient.subscribe(onboardingResponse.getConnectionCriteria().getCommands());

        final SetCapabilityServiceImpl setCapabilityService = new SetCapabilityServiceImpl(mqttClient);
        final SetCapabilitiesParameters setCapabilitiesParameters = new SetCapabilitiesParameters();
        setCapabilitiesParameters.setApplicationId(communicationUnit.getApplicationId());
        setCapabilitiesParameters.setCertificationVersionId(
                communicationUnit.getCertificationVersionId());
        setCapabilitiesParameters.setOnboardingResponse(onboardingResponse);
        final SetCapabilitiesParameters.CapabilityParameters capabilityParameters =
                new SetCapabilitiesParameters.CapabilityParameters();
        capabilityParameters.setDirection(Capabilities.CapabilitySpecification.Direction.SEND_RECEIVE);
        capabilityParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        setCapabilitiesParameters.setCapabilitiesParameters(
                Collections.singletonList(capabilityParameters));
        setCapabilityService.send(setCapabilitiesParameters);

        int nrOfRetries = 0;
        while (!messageHasBeenDelivered && !messageHasArrived && nrOfRetries < 10) {
            LOGGER.debug("Waiting for message to arrive, retrying in 1 second. This is the {} retry.", nrOfRetries);
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
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            messageHasArrived = true;
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            messageHasBeenDelivered = true;
        }
    }
}
