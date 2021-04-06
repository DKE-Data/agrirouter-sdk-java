package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the keystore can not be created. */
public class CouldNotVerifySignatureException extends RuntimeException {

  public CouldNotVerifySignatureException(Exception cause) {
    super(cause);
  }
}
