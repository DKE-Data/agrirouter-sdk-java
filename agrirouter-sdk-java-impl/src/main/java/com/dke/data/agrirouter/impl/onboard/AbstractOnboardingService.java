package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.google.gson.Gson;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractOnboardingService extends EnvironmentalService {

  protected AbstractOnboardingService(Environment environment) {
    super(environment);
  }

  protected OnboardingRequest getOnboardRequest(
      String uuid,
      String applicationId,
      String certificationVersionId,
      String gatewayId,
      CertificationType certificationType) {
    this.getNativeLogger().info("BEGIN | Creating onboard request.");

    OnboardingRequest onboardingRequest = new OnboardingRequest();
    onboardingRequest.setId(uuid);
    onboardingRequest.setApplicationId(applicationId);
    onboardingRequest.setCertificationVersionId(certificationVersionId);
    onboardingRequest.setGatewayId(gatewayId);
    onboardingRequest.setCertificateType(certificationType.getKey());
    onboardingRequest.setUTCTimestamp(UtcTimeService.inThePast(10).toString());
    onboardingRequest.setTimeZone(UtcTimeService.offset());

    this.getNativeLogger().info("END | Creating onboard request.");
    return onboardingRequest;
  }

  /**
   * Generate authorization URL for secured onboarding.
   *
   * @param parameters -
   * @return -
   */
  public String generateAuthorizationUrl(AuthorizationRequestParameters parameters) {
    this.getNativeLogger().info("BEGIN | Generating authorization URL. | '{}'.", parameters);
    String securedOnboardingAuthorizationUrl =
        this.environment.getSecuredOnboardingAuthorizationUrl(
            parameters.getApplicationId(),
            parameters.getResponseType(),
            parameters.getState(),
            parameters.getRedirectUri());
    this.getNativeLogger().info("END | Generating authorization URL. | '{}'.", parameters);
    return securedOnboardingAuthorizationUrl;
  }

  /**
   * Decored onboarding error and return dedicated error object for further analysis.
   *
   * @param error -
   * @return -
   */
  public Optional<OnboardingError> getOnboardingError(String error) {
    this.getNativeLogger().info("BEGIN | Decoding error response. | '{}'.", error);
    Optional<OnboardingError> result =
        StringUtils.isBlank(error) ? Optional.empty() : Optional.of(failSafeGsonParsing(error));
    this.getNativeLogger().info("BEGIN | Decoding error response. | '{}'.", error);
    return result;
  }

  private OnboardingError failSafeGsonParsing(String error) {
    Gson gson = new Gson();
    try {
      return gson.fromJson(error, OnboardingError.class);
    } catch (Exception e) {
      this.getNativeLogger().error("Error parsing error response. | '{}'.", error);
      return OnboardingError.unknownError(error);
    }
  }
}
