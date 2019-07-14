package com.dke.data.agrirouter.impl.messaging;

import org.eclipse.paho.client.mqttv3.IMqttClient;

/** Base class which holds the MQTT client with the connection provided by the provider. */
public class MqttService {

  private final IMqttClient mqttClient;

  public MqttService(IMqttClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  protected IMqttClient getMqttClient() {
    return mqttClient;
  }
}
