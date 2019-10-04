package com.dke.data.agrirouter.api.exception;

public class OnboardingException extends RuntimeException {
  private final String lastError;

  public OnboardingException(String lastError) {
    this.lastError = lastError;
  }
}
