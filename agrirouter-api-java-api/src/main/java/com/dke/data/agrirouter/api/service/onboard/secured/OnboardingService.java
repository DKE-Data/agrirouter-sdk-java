package com.dke.data.agrirouter.api.service.onboard.secured;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.api.service.parameters.SecuredOnboardingParameters;

/** Service for the onboarding process. */
public interface OnboardingService {

  /**
   * Oboarding of an endpoint.
   *
   * @param parameters-
   * @return -
   */
  OnboardingResponse onboard(SecuredOnboardingParameters parameters);

  /**
   * Verify the onboarding request to ensure correct signature and hashing.
   *
   * @param parameters -
   */
  void verify(SecuredOnboardingParameters parameters);

  /**
   * Generating the authorization URL.
   *
   * @param parameters -
   * @return -
   */
  String generateAuthorizationUrl(AuthorizationRequestParameters parameters);
}
