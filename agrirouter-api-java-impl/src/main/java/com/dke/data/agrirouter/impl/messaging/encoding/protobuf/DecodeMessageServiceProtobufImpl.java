package com.dke.data.agrirouter.impl.messaging.encoding.protobuf;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sap.iotservices.common.protobuf.gateway.CommandResponseMessageProtos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/** Internal service implementation. */
public class DecodeMessageServiceProtobufImpl extends DecodeMessageServiceImpl {

  public DecodeMessageResponse decode(CommandResponseMessageProtos.CommandResponseMessage commandResponseMessage)
  {

    if (commandResponseMessage == null) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }
    return this.decode(commandResponseMessage.getMessage().toByteArray());
  }
}
