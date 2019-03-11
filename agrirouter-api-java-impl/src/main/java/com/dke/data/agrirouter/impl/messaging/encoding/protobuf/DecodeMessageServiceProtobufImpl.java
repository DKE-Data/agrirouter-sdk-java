package com.dke.data.agrirouter.impl.messaging.encoding.protobuf;

import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.sap.iotservices.common.protobuf.gateway.CommandResponseMessageProtos;

/** Internal service implementation. */
public class DecodeMessageServiceProtobufImpl extends DecodeMessageServiceImpl {

  public DecodeMessageResponse decode(
      CommandResponseMessageProtos.CommandResponseMessage commandResponseMessage) {

    if (commandResponseMessage == null) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }
    return this.decode(commandResponseMessage.getMessage().toByteArray());
  }
}
