package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Sending messages via MQTT using the given MQTT client. The service itself can not access the
 * queue because connection handling should not be part of the API.
 */
public class SendMessageServiceImpl extends MqttService
    implements SendMessageService, MessageSender {

  public SendMessageServiceImpl(IMqttClient mqttClient) {
    super(mqttClient);
  }

  public void send(SendMessageParameters parameters) {
    parameters.validate();
    try {
      String messageAsJson = this.createMessageBody(parameters);
      byte[] payload = messageAsJson.getBytes();
      this.getMqttClient()
          .publish(
              parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
              new MqttMessage(payload));
    } catch (MqttException e) {
      throw new CouldNotSendMqttMessageException(e);
    }
  }
}
