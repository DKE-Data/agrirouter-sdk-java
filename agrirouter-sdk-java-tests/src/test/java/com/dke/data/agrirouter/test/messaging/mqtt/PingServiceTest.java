package com.dke.data.agrirouter.test.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttClientService;
import com.dke.data.agrirouter.convenience.mqtt.client.MqttOptionService;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.junit.jupiter.api.Test;

class PingServiceTest extends AbstractIntegrationTest {

    @Test
    void sendHealthMessageToPingEndpoint() throws Throwable {
        OnboardingResponse onboardingResponse = OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.MQTT_COMMUNICATION_UNIT);

        MqttOptionService mqttOptionService = new MqttOptionService(new QA() {
        });
        MqttClientService mqttClientService = new MqttClientService(new QA() {
        });
    }


}
