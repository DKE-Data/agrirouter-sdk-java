package com.dke.data.agrirouter.api.mqtt;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

public class HiveMqttClientWrapper implements MqttClientWrapper {

    private final Mqtt3AsyncClient mqttClient;

    public HiveMqttClientWrapper(Mqtt3AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public void publish(String measures, byte[] payload) {
        this.mqttClient.publishWith().topic(measures).payload(payload).send();
    }
}
