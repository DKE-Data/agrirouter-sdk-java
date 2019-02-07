package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

public class SendMessageServiceImpl
    implements SendMessageService, ResponseValidator, MessageSender {

  @Override
  public void send(SendMessageParameters parameters) {
    parameters.validate();
    MessageSenderResponse response = this.sendMessage(parameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
  }
}
