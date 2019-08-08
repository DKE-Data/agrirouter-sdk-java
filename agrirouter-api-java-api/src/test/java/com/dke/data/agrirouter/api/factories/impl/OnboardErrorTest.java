package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

public class OnboardErrorTest {
  @Test
  public void readError() {
    Gson gson = new Gson();
    String result =
        "{\"error\":{\"code\":\"0401\",\"message\":\"Bearer not found.\",\"target\":\"\",\"details\":[]}}";
    OnboardingError onboardingError = gson.fromJson(result, OnboardingError.class);
    assertEquals(onboardingError.getError().getCode(), 401);
  }
}
