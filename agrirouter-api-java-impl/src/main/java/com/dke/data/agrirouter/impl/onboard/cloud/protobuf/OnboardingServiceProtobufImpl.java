package com.dke.data.agrirouter.impl.onboard.cloud.protobuf;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.DecodeMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.protobuf.FetchMessageServiceProtobufImpl;
import com.dke.data.agrirouter.impl.messaging.rest.protobuf.MessageSenderProtobufImpl;
import com.dke.data.agrirouter.impl.onboard.cloud.OnboardingServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.protobuf.EncodeMessageServiceProtobufImpl;
import com.sap.iotservices.common.protobuf.gateway.MeasureProtos;

public class OnboardingServiceProtobufImpl extends OnboardingServiceImpl<MeasureProtos.MeasureRequest.Measure> {

  public OnboardingServiceProtobufImpl() {
    super(new EncodeMessageServiceProtobufImpl(),
      new MessageSenderProtobufImpl(),
      new FetchMessageServiceProtobufImpl(),
      new DecodeMessageServiceProtobufImpl());
  }
}
