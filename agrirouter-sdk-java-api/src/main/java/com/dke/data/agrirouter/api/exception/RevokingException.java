package com.dke.data.agrirouter.api.exception;

import com.dke.data.agrirouter.api.dto.revoke.RevokingError;
import java.util.Optional;

public class RevokingException extends RuntimeException {
  private final RevokingError revokingError;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public RevokingException(Optional<RevokingError> lastError) {
    this.revokingError = lastError.orElse(null);
  }

  public RevokingError getRevokingError() {
    return revokingError;
  }

  @Override
  public String getMessage() {
    return null != revokingError
        ? String.format(
            "There was an error '%s' during the revoking, details were '%s'",
            revokingError.getError().getCode(), revokingError.getError().message)
        : "There was an error during the revoking process.";
  }
}
