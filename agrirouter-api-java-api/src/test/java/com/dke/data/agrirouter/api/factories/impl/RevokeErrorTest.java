package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dke.data.agrirouter.api.dto.revoke.RevokingError;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

public class RevokeErrorTest {
  @Test
  public void readRevokeError() {
    Gson gson = new Gson();
    String errorMessage =
        "{\"error\":{\"code\":\"0100\",\"message\":\"Invalid payload.\",\"target\":\"\",\"details\":[{\"message\":\"should match format \\\"uuid\\\"\"}]}}";
    RevokingError revokingError = gson.fromJson(errorMessage, RevokingError.class);
    assertEquals(revokingError.getError(), 100);
  }
}
