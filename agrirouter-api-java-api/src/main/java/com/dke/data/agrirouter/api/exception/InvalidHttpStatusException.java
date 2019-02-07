package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the expected status does not match the current status. */
public class InvalidHttpStatusException extends RuntimeException {

  public InvalidHttpStatusException(int status) {
    super("Did not expect HTTP status " + status + ".");
  }
}
