package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public interface MessageSender extends MessageBodyCreator {

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
