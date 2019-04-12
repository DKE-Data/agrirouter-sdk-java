package com.dke.data.agrirouter.api.service.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

/** Decoding of messages. */
public interface DecodeMessageService {

  /**
   * Decode a given message using the internal protobuf implementation.
   *
   * @param encodedResponse -
   * @return -
   */
  DecodeMessageResponse decode(String encodedResponse);

  /**
   * Decoding a given message using the internal protobuf implementation.
   *
   * @param message -
   * @return -
   */
  @Deprecated
  MessageOuterClass.Message decode(ByteString message);

  /**
   * Decoding a given Any object containing the messages from the AR using the internal protobuf
   * implementation.
   *
   * @param any -
   * @return -
   */
  MessageOuterClass.Messages decode(Any any);
}
