package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.service.messaging.DeleteMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.Collections;

public class DeleteMessageServiceImpl
    implements DeleteMessageService, MessageSender, ResponseValidator, MessageEncoder {

  private final EncodeMessageService encodeMessageService;

  public DeleteMessageServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
  }

  @Override
  public String send(DeleteMessageParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessageResponse = this.encode(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessageResponse.getEncodedMessage()));
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodedMessageResponse.getApplicationMessageID();
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
