package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.DecodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageConfirmationServiceImpl;

public class MessageConfirmationServiceProtobufImpl extends MessageConfirmationServiceImpl {

  public MessageConfirmationServiceProtobufImpl(Environment environment) {
    super(
        environment,
        new EncodeMessageServiceProtobufImpl(),
        new DecodeMessageServiceProtobufImpl(),
        new MessageSenderProtobufImpl(),
        new MessageQueryServiceProtobufImpl(environment),
        new FetchMessageServiceProtobufImpl());
  }
}
