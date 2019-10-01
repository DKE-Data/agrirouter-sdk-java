package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.OnboardingException;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.gson.Gson;
import java.util.Optional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
public class OnboardingServiceImpl extends AbstractOnboardingService
    implements OnboardingService, ResponseValidator {

  public OnboardingServiceImpl(Environment environment) {
    super(environment);
  }

  @Override
  public OnboardingResponse onboard(OnboardingParameters parameters) {
    this.getNativeLogger().info("BEGIN | Onboarding process. | '{}'.", parameters);
    parameters.validate();

    this.getNativeLogger().debug("Onboard device.");
    OnboardingResponse onboardingResponse =
        this.onboard(
            parameters.getRegistrationCode(), this.createOnboardingRequestBody(parameters));

    this.getNativeLogger().info("END | Onboarding process. | '{}'.", parameters);
    return onboardingResponse;
  }

  private OnboardingRequest createOnboardingRequestBody(OnboardingParameters parameters) {
    this.getNativeLogger().info("BEGIN | Create onboarding request. | '{}'.", parameters);
    OnboardingRequest onboardRequest =
        this.getOnboardRequest(
            parameters.getUuid(),
            parameters.getApplicationId(),
            parameters.getCertificationVersionId(),
            parameters.getGatewayId(),
            parameters.getCertificationType());
    this.getNativeLogger().info("END | Create onboarding request. | '{}'.", parameters);
    return onboardRequest;
  }

  private OnboardingResponse onboard(String registrationCode, OnboardingRequest onboardingRequest) {
    this.getNativeLogger()
        .info("BEGIN | Onboarding process. | '{}', '{}'.", registrationCode, onboardingRequest);
    Response response =
        RequestFactory.bearerTokenRequest(this.environment.getOnboardUrl(), registrationCode)
            .post(Entity.json(onboardingRequest));
    try {
      response.bufferEntity();
      this.assertStatusCodeIsCreated(response.getStatus());
      OnboardingResponse onboardingResponse = response.readEntity(OnboardingResponse.class);

      this.getNativeLogger()
          .info("END | Onboarding process. | '{}', '{}'.", registrationCode, onboardingRequest);
      return onboardingResponse;
    } catch (Exception e) {
      String lastError = response.readEntity(String.class);
      throw new OnboardingException(lastError);
    } finally {
      response.close();
    }
  }

  @Override
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

  @Override
  public Optional<OnboardingError> getLastOnboardingError(String errorResponse) {
    if (errorResponse == null || StringUtils.isBlank(errorResponse)) {
      return Optional.empty();

    } else {
      Gson gson = new Gson();
      return Optional.of(gson.fromJson(errorResponse, OnboardingError.class));
    }
  }
}
