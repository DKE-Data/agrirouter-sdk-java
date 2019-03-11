package com.dke.data.agrirouter.impl.messaging.encoding.protobuf;

import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.google.protobuf.ByteString;
import com.sap.iotservices.common.protobuf.gateway.MeasureRequestMessageProtos;

/** Internal service implementation. */
public class EncodeMessageServiceProtobufImpl extends EncodeMessageServiceImpl {

  @Override
  public EncodeMessageResponse.EncodeMessageResponseProtobuf encode(
      MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters) {
    this.logMethodBegin(messageHeaderParameters, payloadParameters);

    if (null == messageHeaderParameters || null == payloadParameters) {
      throw new IllegalArgumentException("Parameters cannot be NULL");
    }
    messageHeaderParameters.validate();
    payloadParameters.validate();

    byte[] messageBuffer = encodeStreamedMessage(messageHeaderParameters, payloadParameters);

    MeasureRequestMessageProtos.MeasureRequestMessage.Builder measureRequestBuilder =
        MeasureRequestMessageProtos.MeasureRequestMessage.newBuilder();

    measureRequestBuilder.setMessage(ByteString.copyFrom(messageBuffer));
    MeasureRequestMessageProtos.MeasureRequestMessage measureMessageProtobuf;
    measureMessageProtobuf = measureRequestBuilder.build();

    return new EncodeMessageResponse.EncodeMessageResponseProtobuf(
        messageHeaderParameters.applicationMessageId, measureMessageProtobuf);
  }
}
