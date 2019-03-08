package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.MessageHeaderQueryService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryHelper;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageHeaderQueryServiceImpl extends EnvironmentalService
    implements MessageHeaderQueryService, MessageSender {

  private final MessageQueryHelper messageQueryHelper;
  private final MessageSender messageSender;
  private final EncodeMessageService encodeMessageService;

  /**
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     MessageHeaderQueryServiceJSONImpl or MessageHeaderQueryServiceProtobufImpl instead
   */
  @Deprecated
  public MessageHeaderQueryServiceImpl(Environment environment) {

    this(environment, new MessageSenderJSONImpl(), new EncodeMessageServiceJSONImpl());
  }

  public MessageHeaderQueryServiceImpl(
      Environment environment,
      MessageSender messageSender,
      EncodeMessageService encodeMessageService) {
    super(environment);
    this.messageSender = messageSender;
    this.encodeMessageService = encodeMessageService;
    messageQueryHelper =
        new MessageQueryHelper(
            encodeMessageService, messageSender, TechnicalMessageType.DKE_FEED_HEADER_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    String applicationMessageID = this.messageQueryHelper.send(parameters);
    return applicationMessageID;
  }

  @Override
  public FeedResponse.HeaderQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.HeaderQueryResponse.parseFrom(message);
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return messageSender.sendMessage(parameters);
  }
}
