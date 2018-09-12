package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.MessageQueryService;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.helper.MessageQueryHelper;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageQueryServiceImpl extends EnvironmentalService
    implements MessageQueryService,
        MessageSender,
        MessageDecoder<FeedResponse.MessageQueryResponse>,
        ResponseValidator {

  private final MessageQueryHelper messageQueryHelper;

  public MessageQueryServiceImpl(Environment environment) {
    super(environment);
    this.messageQueryHelper =
        new MessageQueryHelper(
            new EncodeMessageServiceImpl(), TechnicalMessageType.DKE_FEED_MESSAGE_QUERY);
  }

  @Override
  public void send(MessageQueryParameters parameters) {
    this.messageQueryHelper.send(parameters);
  }

  @Override
  public FeedResponse.MessageQueryResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return FeedResponse.MessageQueryResponse.parseFrom(message);
  }
}
