package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.MessageHeaderQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryHelperService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageHeaderQueryServiceImpl extends EnvironmentalService
    implements MessageHeaderQueryService, MessageSender {

  private final MessageQueryHelperService messageQueryHelperService;

  public MessageHeaderQueryServiceImpl(Environment environment) {
    super(environment);
    messageQueryHelperService =
        new MessageQueryHelperService(
            new EncodeMessageServiceImpl(), TechnicalMessageType.DKE_FEED_HEADER_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    String applicationMessageID = this.messageQueryHelperService.send(parameters);
    return applicationMessageID;
  }

  @Override
  public FeedResponse.HeaderQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.HeaderQueryResponse.parseFrom(message);
  }
}
