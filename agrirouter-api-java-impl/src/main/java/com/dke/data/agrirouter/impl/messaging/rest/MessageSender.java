package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import javax.ws.rs.core.Response;

public interface MessageSender<T> {

  T createSendMessageRequest(SendMessageParameters parameters);

  MessageSenderResponse sendMessage(SendMessageParameters parameters);

  class MessageSenderResponse {

    private final Response nativeResponse;

    public MessageSenderResponse(Response nativeResponse) {
      this.nativeResponse = nativeResponse;
    }

    public Response getNativeResponse() {
      return nativeResponse;
    }
  }
}
