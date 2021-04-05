package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if there are any problems with the message decoding itself - possible I/O
 * exceptions during byte array output stream generation for example.
 */
public class CouldNotDecodeMessageException extends RuntimeException {

  public CouldNotDecodeMessageException(Throwable cause) {
    super(cause);
  }
}
