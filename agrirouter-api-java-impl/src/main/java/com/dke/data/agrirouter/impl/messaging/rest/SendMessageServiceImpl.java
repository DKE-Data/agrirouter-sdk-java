package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

public class SendMessageServiceImpl<SenderType>
    implements SendMessageService, ResponseValidator, MessageSender<SenderType> {

  private MessageSender<SenderType> messageSender;

  @Override
  public String send(SendMessageParameters parameters) {
    parameters.validate();
    MessageSenderResponse response = this.sendMessage(parameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return parameters.getEncodeMessageResponse().getApplicationMessageID();
  }

  @Override
  public SenderType createSendMessageRequest(SendMessageParameters parameters) {
    return (SenderType) messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
