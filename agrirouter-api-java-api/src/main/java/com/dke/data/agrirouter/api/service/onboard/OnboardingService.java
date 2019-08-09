package com.dke.data.agrirouter.api.service.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import java.util.Optional;

/** Service for the onboarding process. */
public interface OnboardingService {

  /**
   * Oboarding of an endpoint.
   *
   * @param parameters-
   * @return -
   */
  OnboardingResponse onboard(OnboardingParameters parameters);

  String generateAuthorizationUrl(AuthorizationRequestParameters parameters);

  /** @return The last error, if the last onboarding failed as a String */
  String getLastErrorAsString();

  /** @return The last error, if the last onboarding failed as object read from the JSON String */
  Optional<OnboardingError> getLastError();
}
