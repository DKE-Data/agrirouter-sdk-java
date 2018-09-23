package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.*;
import com.dke.data.agrirouter.api.service.messaging.MessageHeaderQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageHeaderQueryServiceImpl extends EnvironmentalService
    implements MessageHeaderQueryService, MessageSender {

  private final MessageQueryService messageQueryService;

  public MessageHeaderQueryServiceImpl(Environment environment) {
    super(environment);
    messageQueryService =
        new MessageQueryService(
            new EncodeMessageServiceImpl(), TechnicalMessageType.DKE_FEED_HEADER_QUERY);
  }

  @Override
  public void send(MessageQueryParameters parameters)
      throws InvalidUrlForRequestException, UnauthorizedRequestException, ForbiddenRequestException,
          CouldNotEncodeMessageException, CouldNotCreateDynamicKeyStoreException,
          UnexpectedHttpStatusException {
    this.messageQueryService.send(parameters);
  }

  @Override
  public FeedResponse.HeaderQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.HeaderQueryResponse.parseFrom(message);
  }
}
