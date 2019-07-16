package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the keystore can not be created. */
public class CouldNotCreateSignatureException extends RuntimeException {

  public CouldNotCreateSignatureException(Exception cause) {
    super(cause);
  }
}
