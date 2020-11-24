package com.dke.data.agrirouter.api.exception;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import java.util.Optional;

public class OnboardingException extends RuntimeException {
  private final OnboardingError onboardingError;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public OnboardingException(Optional<OnboardingError> onboardingError) {
    this.onboardingError = onboardingError.orElse(null);
  }

  public OnboardingError getOnboardingError() {
    return onboardingError;
  }

  @Override
  public String getMessage() {
    return null != onboardingError
        ? String.format(
            "There was an error '%s' during the onboarding process, details were '%s'",
            onboardingError.getError().getCode(), onboardingError.getError().message)
        : "There was an error during the onboarding process.";
  }

}
