package com.dke.data.agrirouter.impl.onboard.cloud.protobuf;

import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.DecodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.protobuf.FetchMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.protobuf.MessageSenderProtobufImpl;
import com.dke.data.agrirouter.impl.onboard.cloud.OffboardingServiceImpl;

public class OffboardingServiceProtobufImpl extends OffboardingServiceImpl {

  public OffboardingServiceProtobufImpl() {
    super(
        new EncodeMessageServiceProtobufImpl(),
        new MessageSenderProtobufImpl(),
        new FetchMessageServiceProtobufImpl(),
        new DecodeMessageServiceProtobufImpl());
  }
}
