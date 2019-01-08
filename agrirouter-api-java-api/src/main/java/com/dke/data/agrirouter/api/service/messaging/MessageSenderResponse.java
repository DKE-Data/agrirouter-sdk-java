package com.dke.data.agrirouter.api.service.messaging;

import javax.ws.rs.core.Response;

public class MessageSenderResponse {

  private final Response nativeResponse;

  public MessageSenderResponse(Response nativeResponse) {
    this.nativeResponse = nativeResponse;
  }

  public Response getNativeResponse() {
    return nativeResponse;
  }
}
