package com.dke.data.agrirouter.impl;

import java.util.UUID;

/** Service to provide a unique chunk context id. */
public class ChunkContextIdService {

  /**
   * Generate a unique id by using the internal UUID implementation.
   *
   * @return -
   */
  public static String generateChunkContextId() {
    return UUID.randomUUID().toString();
  }
}
