package com.dke.data.agrirouter.impl.messaging;

import com.dke.data.agrirouter.api.mqtt.MqttClientWrapper;

/**
 * Base class which holds the MQTT client with the connection provided by the provider.
 */
public class MqttService {

    private final MqttClientWrapper mqttClientWrapper;

    public MqttService(MqttClientWrapper mqttClientWrapper) {
        this.mqttClientWrapper = mqttClientWrapper;
    }

    protected MqttClientWrapper getMqttClient() {
        return mqttClientWrapper;
    }
}
