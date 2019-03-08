package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if there are any problems with the message encoding itself - possible I/O
 * exceptions during byte array output stream generation for example.
 */
public class WrongFormatForMessageException extends RuntimeException {

  public WrongFormatForMessageException(String cause) {
    super(cause);
  }
}
