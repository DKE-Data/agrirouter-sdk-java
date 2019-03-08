package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SendMessageServiceImpl;

public class SendMessageServiceProtobufImpl extends SendMessageServiceImpl {

  public SendMessageServiceProtobufImpl() {
    super(new MessageSenderProtobufImpl(), new EncodeMessageServiceProtobufImpl());
  }
}
