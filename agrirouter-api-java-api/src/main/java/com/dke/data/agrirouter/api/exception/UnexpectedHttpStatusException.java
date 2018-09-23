package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the expected status does not match the current status. */
public class UnexpectedHttpStatusException extends Exception {

  public UnexpectedHttpStatusException(int status, int exceptedHttpStatus) {
    super(
        "Did not expect this HTTP status. Status was "
            + status
            + ", expected status was "
            + exceptedHttpStatus);
  }
}
