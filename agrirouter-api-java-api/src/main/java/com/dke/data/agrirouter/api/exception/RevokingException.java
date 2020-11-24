package com.dke.data.agrirouter.api.exception;

public class RevokingException extends RuntimeException {
  private final String lastError;

  public RevokingException(String lastError) {
    this.lastError = lastError;
  }

  public String getLastError() {
    return lastError;
  }
}
