package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageQueryServiceImpl;
import com.sap.iotservices.common.protobuf.gateway.MeasureProtos;

public class MessageQueryServiceProtobufImpl
    extends MessageQueryServiceImpl<MeasureProtos.MeasureRequest.Measure> {
  public MessageQueryServiceProtobufImpl(Environment environment) {
    super(environment, new MessageSenderProtobufImpl(), new EncodeMessageServiceProtobufImpl());
  }
}
