package com.dke.data.agrirouter.impl.messaging.rest.json;

import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequestJSON;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.exception.WrongFormatForMessageException;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MessageSenderJSONImpl implements MessageSender {

  public MessageRequestJSON createSendMessageRequest(SendMessageParameters parameters) {
    parameters.validate();
    EncodeMessageResponse.EncodeMessageResponseJSON encodeMessageResponseJSON = null;
    if (parameters.getEncodeMessageResponse()
        instanceof EncodeMessageResponse.EncodeMessageResponseJSON) {
      encodeMessageResponseJSON =
          (EncodeMessageResponse.EncodeMessageResponseJSON) parameters.getEncodeMessageResponse();
    } else {
      throw new WrongFormatForMessageException("Trying to pass Protobuf Message to JSON MessageSender");
    }

    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setSensorAlternateId(
        parameters.getOnboardingResponse().getSensorAlternateId());
    sendMessageRequest.setCapabilityAlternateId(
        parameters.getOnboardingResponse().getCapabilityAlternateId());
    List<Message> messages = new ArrayList<>();
    Collections.singletonList(encodeMessageResponseJSON)
        .forEach(
            messageToSend -> {
              Message message = new Message();
              message.setMessage(messageToSend.getEncodedMessageBase64());
              message.setTimestamp("" + UtcTimeService.now().toEpochSecond());
              messages.add(message);
            });
    sendMessageRequest.setMessages(messages);
    return new MessageRequestJSON(sendMessageRequest);
  }

  public MessageSender.MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    MessageRequestJSON messageRequest = this.createSendMessageRequest(parameters);
    Entity<SendMessageRequest> entity = Entity.json(messageRequest.getSendMessageRequest());

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
