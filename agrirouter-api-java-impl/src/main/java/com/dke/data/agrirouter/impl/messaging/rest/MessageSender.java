package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.messaging.MessageBodyCreator;
import java.util.Objects;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/** Interface with default implementations for sending messages. */
public interface MessageSender extends MessageBodyCreator {

  /**
   * Send message to the AR using the given parameters.
   *
   * @param parameters -
   * @return The actual HTTP response from the AR for this request. This is not the ACK that can be
   *     fetched afterwards.
   */
  default MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    Response response =
        RequestFactory.securedRequest(
                Objects.requireNonNull(parameters.getOnboardingResponse())
                    .getConnectionCriteria()
                    .getMeasures(),
                parameters.getOnboardingResponse().getAuthentication().getCertificate(),
                parameters.getOnboardingResponse().getAuthentication().getSecret(),
                CertificationType.valueOf(
                    parameters.getOnboardingResponse().getAuthentication().getType()))
            .post(Entity.json(this.createSendMessageRequest(parameters)));
    return new MessageSenderResponse(response);
  }

  /** Message response. */
  class MessageSenderResponse {

    /** Wrapped HTTP response for the request. */
    private final Response nativeResponse;

    private MessageSenderResponse(Response nativeResponse) {
      this.nativeResponse = nativeResponse;
    }

    public Response getNativeResponse() {
      return nativeResponse;
    }
  }
}
