package com.dke.data.agrirouter.api.mqtt;

public interface MqttClientWrapper {

    void publish(String measures, byte[] payload);
}
