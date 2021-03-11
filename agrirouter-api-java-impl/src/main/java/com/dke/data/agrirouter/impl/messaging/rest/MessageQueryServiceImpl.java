package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.messaging.http.MessageQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryHelperService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageQueryServiceImpl extends EnvironmentalService
    implements MessageQueryService,
        MessageSender,
        MessageDecoder<FeedResponse.MessageQueryResponse> {

  private final MessageQueryHelperService messageQueryHelperService;

  public MessageQueryServiceImpl(Environment environment) {
    super(environment);
    messageQueryHelperService =
        new MessageQueryHelperService(
            new EncodeMessageServiceImpl(), TechnicalMessageType.DKE_FEED_MESSAGE_QUERY);
  }

  @Override
  public String send(MessageQueryParameters parameters) {
    return messageQueryHelperService.send(parameters);
  }

  @Override
  public HttpAsyncMessageSendingResult sendAsync(MessageQueryParameters parameters) {
    return messageQueryHelperService.sendAsync(parameters);
  }

  @Override
  public FeedResponse.MessageQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.MessageQueryResponse.parseFrom(message);
  }
}
