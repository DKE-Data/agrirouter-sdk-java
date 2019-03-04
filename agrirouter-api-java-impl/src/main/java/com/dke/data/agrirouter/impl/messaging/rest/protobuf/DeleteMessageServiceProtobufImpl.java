package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.DeleteMessageServiceImpl;

public class DeleteMessageServiceProtobufImpl extends DeleteMessageServiceImpl {

  public DeleteMessageServiceProtobufImpl() {
    super(new MessageSenderProtobufImpl(), new EncodeMessageServiceProtobufImpl());
  }
}
