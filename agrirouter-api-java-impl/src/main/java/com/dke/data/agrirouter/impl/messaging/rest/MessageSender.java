package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public interface MessageSender {

  default String getMessageAsJson(SendMessageParameters parameters) {
    parameters.validate();
    return new Gson().toJson(this.createSendMessageRequest(parameters));
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
        .getMessages()
        .forEach(
            messageToSend -> {
              Message message = new Message();
              message.setMessage(messageToSend.getEncodedMessage());
              message.setTimestamp("" + UtcTimeService.now().toEpochSecond());
              messages.add(message);
            });
    sendMessageRequest.setMessages(messages);
    return sendMessageRequest;
  }

  default MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    Response response =
        RequestFactory.securedRequest(
                parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
                parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                parameters.getOnboardingResponse().getAuthentication().getSecret(),
                CertificationType.valueOf(
                    parameters.getOnboardingResponse().getAuthentication().getType()))
            .post(Entity.json(this.createSendMessageRequest(parameters)));
    return new MessageSenderResponse(response);
  }

  class MessageSenderResponse {

    private final Response nativeResponse;

    private MessageSenderResponse(Response nativeResponse) {
      this.nativeResponse = nativeResponse;
    }

    public Response getNativeResponse() {
      return nativeResponse;
    }
  }
}
