package com.dke.data.agrirouter.impl.messaging.rest;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.List;
import java.util.Optional;

public abstract class MessageFetcher implements ResponseValidator {

  String EMPTY_CONTENT = "[]";

  public Optional<List<FetchMessageResponse>> fetch(
      OnboardingResponse onboardingResponse, int maxTries, long interval) {
    FetchMessageParameters fetchMessageParameters = new FetchMessageParameters();
    fetchMessageParameters.setOnboardingResponse(onboardingResponse);
    return this.fetch(fetchMessageParameters, maxTries, interval);
  }

  public Optional<List<FetchMessageResponse>> fetch(
      FetchMessageParameters parameters, int maxTries, long interval) {
    return poll(parameters, maxTries, interval);
  }

  public Optional<List<FetchMessageResponse>> poll(
      FetchMessageParameters parameters, int maxTries, long interval) {
    // Has to be implemented in
    return null;
  };
}
