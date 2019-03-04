package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MessageSenderJSONImpl implements MessageSender<SendMessageRequest> {

  public SendMessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    parameters.validate();
    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setSensorAlternateId(
        parameters.getOnboardingResponse().getSensorAlternateId());
    sendMessageRequest.setCapabilityAlternateId(
        parameters.getOnboardingResponse().getCapabilityAlternateId());
    List<Message> messages = new ArrayList<>();
    Collections.singletonList(parameters.getEncodeMessageResponse().getEncodedMessageBase64())
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

  public MessageSender.MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    Entity<SendMessageRequest> entity = Entity.json(
      this.createSendMessageRequest(parameters)
    );

    Response response =
        RequestFactory.securedJSONRequest(
                parameters.getOnboardingResponse().getConnectionCriteria().getMeasures(),
                parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                parameters.getOnboardingResponse().getAuthentication().getSecret(),
                CertificationType.valueOf(
                    parameters.getOnboardingResponse().getAuthentication().getType()))
            .post(entity);
    return new MessageSender.MessageSenderResponse(response);
  }
}
