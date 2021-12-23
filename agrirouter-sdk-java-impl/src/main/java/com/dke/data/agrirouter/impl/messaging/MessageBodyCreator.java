package com.dke.data.agrirouter.impl.messaging;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.gson.MessageTypeAdapter;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface MessageBodyCreator {

  default String createMessageBody(SendMessageParameters parameters) {
    parameters.trimAndValidate();
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Message.class, new MessageTypeAdapter());
      return gsonBuilder.create().toJson(this.createSendMessageRequest(parameters));
  }

  default SendMessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    parameters.trimAndValidate();
    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setSensorAlternateId(
        Objects.requireNonNull(parameters.getOnboardingResponse()).getSensorAlternateId());
    sendMessageRequest.setCapabilityAlternateId(
        parameters.getOnboardingResponse().getCapabilityAlternateId());
    List<Message> messages = new ArrayList<>();
    Objects.requireNonNull(parameters
                    .getEncodedMessages())
        .forEach(
            messageToSend -> {
              Message message = new Message();
              message.setMessage(messageToSend);
              message.setTimestamp("" + UtcTimeService.now().toEpochSecond());
              messages.add(message);
            });
    sendMessageRequest.setMessages(messages);
    return sendMessageRequest;
  }
}
