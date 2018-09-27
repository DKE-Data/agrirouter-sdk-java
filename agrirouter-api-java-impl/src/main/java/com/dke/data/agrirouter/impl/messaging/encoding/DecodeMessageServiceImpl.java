package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
public class DecodeMessageServiceImpl extends NonEnvironmentalService
    implements DecodeMessageService {

  @Override
  public DecodeMessageResponse decode(String encodedResponse) {
    this.logMethodBegin(encodedResponse);

    if (StringUtils.isBlank(encodedResponse)) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }

    try {
      return this.parseResponse(encodedResponse);
    } catch (IOException e) {
      throw new CouldNotDecodeMessageException(e);
    }
  }

  private DecodeMessageResponse parseResponse(String encodedResponse) throws IOException {
    byte[] decodedBytes = getDecodedBytes(encodedResponse);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
    Response.ResponseEnvelope responseEnvelope = this.parseResponseEnvelope(inputStream);
    Response.ResponsePayloadWrapper responsePayloadWrapper = this.parseResponsePayloadWrapper(inputStream);
    DecodeMessageResponse decodeMessageResponse =
            this.getDecodedMessageResponse(responseEnvelope, responsePayloadWrapper);

    this.logMethodEnd(decodeMessageResponse);
    return decodeMessageResponse;
  }

  private byte[] getDecodedBytes(String encodedResponse) {
    this.getNativeLogger().trace("Decoding byte array.");
    return Base64.getDecoder().decode(encodedResponse);
  }

  private Response.ResponseEnvelope parseResponseEnvelope
          (ByteArrayInputStream inputStream) throws IOException {
    this.getNativeLogger().trace("Parse response envelope.");
    return agrirouter.response.Response.ResponseEnvelope.parseDelimitedFrom(inputStream);
  }

  private Response.ResponsePayloadWrapper parseResponsePayloadWrapper
          (ByteArrayInputStream inputStream) throws IOException {
    this.getNativeLogger().trace("Parse response payload wrapper.");
    return agrirouter.response.Response.ResponsePayloadWrapper.parseDelimitedFrom(inputStream);
  }

  private DecodeMessageResponse getDecodedMessageResponse(
          Response.ResponseEnvelope responseEnvelope,
          Response.ResponsePayloadWrapper responsePayloadWrapper) {
    DecodeMessageResponse decodeMessageResponse = new DecodeMessageResponse();
    decodeMessageResponse.setResponseEnvelope(responseEnvelope);
    decodeMessageResponse.setResponsePayloadWrapper(responsePayloadWrapper);
    return decodeMessageResponse;
  }

  @Override
  public MessageOuterClass.Message decode(ByteString message) {
    try {
      this.logMethodBegin(message);

      this.getNativeLogger().trace("Decoding byte string.");
      MessageOuterClass.Message decodedMessage = MessageOuterClass.Message.parseFrom(message);

      this.logMethodEnd(decodedMessage);
      return decodedMessage;
    } catch (InvalidProtocolBufferException e) {
      throw new CouldNotDecodeMessageException(e);
    }
  }
}
