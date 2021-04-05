package com.dke.data.agrirouter.impl.messaging;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.gson.MessageTypeAdapter;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public interface MessageBodyCreator {

  default String createMessageBody(SendMessageParameters parameters) {
    parameters.validate();
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Message.class, new MessageTypeAdapter());
    String json = gsonBuilder.create().toJson(this.createSendMessageRequest(parameters));
    return json;
  }

  default SendMessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    parameters.validate();
    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setSensorAlternateId(
        parameters.getOnboardingResponse().getSensorAlternateId());
    sendMessageRequest.setCapabilityAlternateId(
        parameters.getOnboardingResponse().getCapabilityAlternateId());
    List<Message> messages = new ArrayList<>();
    parameters
        .getEncodedMessages()
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
