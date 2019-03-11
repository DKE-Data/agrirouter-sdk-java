package com.dke.data.agrirouter.api.exception;

public class CouldNotDecodeAuthorizationURIException extends RuntimeException {
  public CouldNotDecodeAuthorizationURIException(String cause) {
    super("Could not Decode URI: " + cause);
  }
}
