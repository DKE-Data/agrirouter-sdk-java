package com.dke.data.agrirouter.impl.messaging.rest.protobuf;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SetCapabilityServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.sap.iotservices.common.protobuf.gateway.MeasureProtos;

public class SetCapabilityServiceProtobufImpl
    extends SetCapabilityServiceImpl<MeasureProtos.MeasureRequest.Measure> {
  public SetCapabilityServiceProtobufImpl(Environment environment) {
    super(environment, new EncodeMessageServiceJSONImpl(), new MessageSenderJSONImpl());
  }
}
