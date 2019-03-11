package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageHeaderQueryServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;

public class MessageHeaderQueryServiceProtobufImpl extends MessageHeaderQueryServiceImpl {

  public MessageSender messageSender;

  public MessageHeaderQueryServiceProtobufImpl(Environment environment) {
    super(environment, new MessageSenderProtobufImpl(), new EncodeMessageServiceProtobufImpl());
  }
}
