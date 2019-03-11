package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DecodeMessageServiceImpl extends NonEnvironmentalService
    implements DecodeMessageService {

  @Override
  public DecodeMessageResponse decode(byte[] decodedBytes) {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

      this.getNativeLogger().trace("Parse response envelope.");
      agrirouter.response.Response.ResponseEnvelope responseEnvelope =
          agrirouter.response.Response.ResponseEnvelope.parseDelimitedFrom(inputStream);

      this.getNativeLogger().trace("Parse response payload wrapper.");
      agrirouter.response.Response.ResponsePayloadWrapper responsePayloadWrapper =
          agrirouter.response.Response.ResponsePayloadWrapper.parseDelimitedFrom(inputStream);
      DecodeMessageResponse decodeMessageResponse = new DecodeMessageResponse();
      decodeMessageResponse.setResponseEnvelope(responseEnvelope);
      decodeMessageResponse.setResponsePayloadWrapper(responsePayloadWrapper);

      this.logMethodEnd(decodeMessageResponse);
      return decodeMessageResponse;
    } catch (IOException e) {
      throw new CouldNotDecodeMessageException(e);
    }
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
