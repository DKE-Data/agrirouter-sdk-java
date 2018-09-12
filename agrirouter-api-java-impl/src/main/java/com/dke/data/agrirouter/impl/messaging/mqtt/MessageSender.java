package com.dke.data.agrirouter.impl.messaging.mqtt;

import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.google.gson.Gson;

public interface MessageSender {

  default String createSendMessageRequest(SendMessageParameters parameters) {
    parameters.validate();
    return new Gson().toJson(this.createSendMessageRequest(parameters));
  }
}
