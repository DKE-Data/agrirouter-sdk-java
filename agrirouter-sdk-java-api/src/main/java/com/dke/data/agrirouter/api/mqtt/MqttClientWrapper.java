package com.dke.data.agrirouter.api.mqtt;

/**
 * Interface representing an MQTT client wrapper for publishing messages.
 * Implementations of this interface should define how to publish messages
 * to a specific topic with a given payload.
 */
public interface MqttClientWrapper {

    /**
     * Publishes a message to a specific topic with a given payload.
     *
     * @param measures the topic to which the message should be published
     * @param payload  the byte array representing the content of the message
     */
    void publish(String measures, byte[] payload);
}
