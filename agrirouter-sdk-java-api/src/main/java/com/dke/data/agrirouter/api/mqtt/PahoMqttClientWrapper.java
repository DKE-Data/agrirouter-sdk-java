package com.dke.data.agrirouter.api.mqtt;

import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PahoMqttClientWrapper implements MqttClientWrapper {

    private final IMqttClient mqttClient;

    public PahoMqttClientWrapper(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public void publish(String measures, byte[] payload) {
        try {
            this.mqttClient.publish(measures, new MqttMessage(payload));
        } catch (MqttException e) {
            throw new CouldNotSendMqttMessageException(e);
        }
    }

}
