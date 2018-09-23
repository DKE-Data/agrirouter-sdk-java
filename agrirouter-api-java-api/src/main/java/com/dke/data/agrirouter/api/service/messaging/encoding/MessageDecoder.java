package com.dke.data.agrirouter.api.service.messaging.encoding;

import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public interface MessageDecoder<T> {

  default T decode(ByteString message) throws CouldNotDecodeMessageException {
    try {
      return unsafeDecode(message);
    } catch (InvalidProtocolBufferException e) {
      throw new CouldNotDecodeMessageException(e);
    }
  }

  T unsafeDecode(ByteString message) throws InvalidProtocolBufferException;
}
