package com.dke.data.agrirouter.impl.common;

import java.util.UUID;

/** Service to provide a unique state id. */
public class StateIdService {

  /**
   * Generate a unique state id by using the internal UUID implementation.
   *
   * @return -
   */
  public static String generateState() {
    return UUID.randomUUID().toString();
  }
}
