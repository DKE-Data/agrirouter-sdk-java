package com.dke.data.agrirouter.impl.messaging;

import org.eclipse.paho.client.mqttv3.IMqttClient;

/** Base class which holds the MQTT client with the connection provided by the provider. */
class MqttService {

  private final IMqttClient mqttClient;

  MqttService(IMqttClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  IMqttClient getMqttClient() {
    return mqttClient;
  }
}
