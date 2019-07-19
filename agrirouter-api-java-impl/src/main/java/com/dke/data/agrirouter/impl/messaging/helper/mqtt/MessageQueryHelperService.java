package com.dke.data.agrirouter.impl.messaging.helper.mqtt;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.CouldNotSendMqttMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.MqttService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import java.util.Collections;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageQueryHelperService extends MqttService
    implements MessageSender, MessageEncoder {

  private final EncodeMessageService encodeMessageService;
  private final TechnicalMessageType technicalMessageType;

  public MessageQueryHelperService(
      IMqttClient mqttClient,
      EncodeMessageService encodeMessageService,
      TechnicalMessageType technicalMessageType) {
    super(mqttClient);
    this.logMethodBegin();
    this.encodeMessageService = encodeMessageService;
    this.technicalMessageType = technicalMessageType;
    this.logMethodEnd();
  }

  public String send(MessageQueryParameters parameters) {
    this.logMethodBegin(parameters);

    this.getNativeLogger().trace("Validate parameters.");
    parameters.validate();
    try {
      this.getNativeLogger().trace("Encode message.");
      EncodedMessage encodedMessage = this.encode(this.technicalMessageType, parameters);

      this.getNativeLogger().trace("Build message parameters.");
      SendMessageParameters sendMessageParameters = new SendMessageParameters();
      sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
      sendMessageParameters.setEncodedMessages(
          Collections.singletonList(encodedMessage.getEncodedMessage()));

      this.getNativeLogger().trace("Send and fetch message response.");
      String messageAsJson = this.createMessageBody(sendMessageParameters);
      byte[] payload = messageAsJson.getBytes();
      this.getMqttClient()
          .publish(
              parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
              new MqttMessage(payload));
      return encodedMessage.getApplicationMessageID();
    } catch (MqttException e) {
      throw new CouldNotSendMqttMessageException(e);
    }
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
