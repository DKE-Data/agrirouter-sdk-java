package com.dke.data.agrirouter.impl.messaging.encoding.json;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
public class DecodeMessageServiceJSONImpl extends DecodeMessageServiceImpl {


  public DecodeMessageResponse decode(String encodedResponseString) {
    this.logMethodBegin(encodedResponseString);

    if (StringUtils.isBlank(encodedResponseString)) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }


    this.getNativeLogger().trace("Decoding byte array.");
    byte[] decodedBytes = Base64.getDecoder().decode(encodedResponseString);
    return this.decode(decodedBytes);
  }
}
