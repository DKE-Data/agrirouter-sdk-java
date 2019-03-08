package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;

public class SendMessageServiceImpl
    implements SendMessageService, ResponseValidator, MessageSender {

  private MessageSender messageSender;
  private EncodeMessageService encodeMessageService;

  /**
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     SendMessageServiceJSONImpl or SendMessageServiceProtobufImpl instead
   */
  @Deprecated
  public SendMessageServiceImpl() {
    this(new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }

  public SendMessageServiceImpl(
      MessageSender messageSender, EncodeMessageService encodeMessageService) {
    this.messageSender = messageSender;
    this.encodeMessageService = encodeMessageService;
  }

  @Override
  public void send(SendMessageParameters parameters) {
    parameters.validate();
    MessageSenderResponse response = this.sendMessage(parameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
