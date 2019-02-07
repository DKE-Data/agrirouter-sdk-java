package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.FetchMessageParameters;
import java.util.List;
import java.util.Optional;

/** Service for fetching messages for the given endpoint. */
public interface FetchMessageService {

  /**
   * Fetch messages using the given onboarding response using the number of tries and the given
   * interval.
   *
   * @param onboardingResponse -
   * @param maxTries -
   * @param interval -
   * @return Native response.
   */
  Optional<List<FetchMessageResponse>> fetch(
      OnboardingResponse onboardingResponse, int maxTries, long interval);

  /**
   * Fetch messages using the given parameters using the number of tries and the given interval.
   *
   * @param parameters -
   * @param maxTries -
   * @param interval -
   * @return Native response.
   */
  Optional<List<FetchMessageResponse>> fetch(
      FetchMessageParameters parameters, int maxTries, long interval);

  FetchMessageResponse parseJson(byte[] json);
}
