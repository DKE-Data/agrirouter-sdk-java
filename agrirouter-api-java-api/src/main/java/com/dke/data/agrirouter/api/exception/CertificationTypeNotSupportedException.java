package com.dke.data.agrirouter.api.exception;

import com.dke.data.agrirouter.api.enums.CertificationType;

/** Will be thrown if the certification type is not known. */
public class CertificationTypeNotSupportedException extends Exception {

  private final CertificationType unknown;

  public CertificationTypeNotSupportedException(CertificationType unknown) {
    this.unknown = unknown;
  }

  @Override
  public String getMessage() {
    return this.unknown + " is unknown and therefore not supported.";
  }
}
