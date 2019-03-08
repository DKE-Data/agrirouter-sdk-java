package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryHelper;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageQueryServiceImpl extends EnvironmentalService
    implements com.dke.data.agrirouter.api.service.messaging.MessageQueryService, MessageSender {

  private final MessageSender messageSender;
  private final EncodeMessageService encodeMessageService;
  private final MessageQueryHelper messageQueryHelper;

  /**
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     MessageQueryServiceJSONImpl or MessageQueryServiceProtobufImpl instead
   */
  @Deprecated
  public MessageQueryServiceImpl(Environment environment) {

    this(environment, new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }

  public MessageQueryServiceImpl(
      Environment environment,
      MessageSender messageSender,
      EncodeMessageService encodeMessageService) {
    super(environment);
    this.messageSender = messageSender;
    this.encodeMessageService = encodeMessageService;
    this.messageQueryHelper =
        new MessageQueryHelper(
            this.encodeMessageService,
            this.messageSender,
            TechnicalMessageType.DKE_FEED_MESSAGE_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    String applicationMessageID = this.messageQueryHelper.send(parameters);
    return applicationMessageID;
  }

  @Override
  public FeedResponse.MessageQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.MessageQueryResponse.parseFrom(message);
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return this.messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageQueryHelper.sendMessage(parameters);
  }
}
