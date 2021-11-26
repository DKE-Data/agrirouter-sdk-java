package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.http.MessageHeaderQueryService;
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
            new EncodeMessageServiceImpl(), SystemMessageType.DKE_FEED_HEADER_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    return this.messageQueryHelperService.send(parameters);
  }

  @Override
  public HttpAsyncMessageSendingResult sendAsync(MessageQueryParameters parameters) {
    return messageQueryHelperService.sendAsync(parameters);
  }

  @Override
  public FeedResponse.HeaderQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.HeaderQueryResponse.parseFrom(message);
  }
}
