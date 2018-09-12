package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
public class DecodeMessageServiceImpl implements DecodeMessageService {

  @Override
  public DecodeMessageResponse decode(String encodedResponse) {
    if (StringUtils.isBlank(encodedResponse)) {
      throw new IllegalArgumentException("Please provide a valid encoded response.");
    }
    try {
      byte[] decodedBytes = Base64.getDecoder().decode(encodedResponse);
      ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
      agrirouter.response.Response.ResponseEnvelope responseEnvelope =
          agrirouter.response.Response.ResponseEnvelope.parseDelimitedFrom(inputStream);
      agrirouter.response.Response.ResponsePayloadWrapper responsePayloadWrapper =
          agrirouter.response.Response.ResponsePayloadWrapper.parseDelimitedFrom(inputStream);
      DecodeMessageResponse decodeMessageResponse = new DecodeMessageResponse();
      decodeMessageResponse.setResponseEnvelope(responseEnvelope);
      decodeMessageResponse.setResponsePayloadWrapper(responsePayloadWrapper);
      return decodeMessageResponse;
    } catch (IOException e) {
      throw new CouldNotDecodeMessageException(e);
    }
  }

  @Override
  public MessageOuterClass.Message decode(ByteString message) {
    try {
      return MessageOuterClass.Message.parseFrom(message);
    } catch (InvalidProtocolBufferException e) {
      throw new CouldNotDecodeMessageException(e);
    }
  }
}
