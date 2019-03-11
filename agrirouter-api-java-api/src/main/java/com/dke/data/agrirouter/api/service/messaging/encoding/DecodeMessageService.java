package com.dke.data.agrirouter.api.service.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.google.protobuf.ByteString;

/** Decoding of messages. */
public interface DecodeMessageService {

  /**
   * Decode a given message using the internal protobuf implementation.
   *
   * @param encodedResponse -
   * @return -
   */
  DecodeMessageResponse decode(byte[] encodedResponse);

  /**
   * Decoding a given message using the internal protobuf implementation.
   *
   * @param message -
   * @return -
   */
  MessageOuterClass.Message decode(ByteString message);
}
