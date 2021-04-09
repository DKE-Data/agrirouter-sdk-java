package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the keystore can not be created. */
public class CouldNotDecodeHexException extends RuntimeException {

  public CouldNotDecodeHexException(Exception cause) {
    super(cause);
  }
}
