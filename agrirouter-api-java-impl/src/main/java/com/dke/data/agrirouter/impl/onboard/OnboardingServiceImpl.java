package com.dke.data.agrirouter.impl.onboard;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.AuthenticationUrlParameters;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;

/** Internal service implementation. */
public class OnboardingServiceImpl extends AbstractOnboardingService
    implements OnboardingService, ResponseValidator {

  public OnboardingServiceImpl(Environment environment) {
    super(environment);
  }

  @Override
  public OnboardingResponse onboard(OnboardingParameters parameters) {
    parameters.validate();
    return this.onboard(
        parameters.getRegistrationCode(), this.createOnboardingRequestBody(parameters));
  }

  private OnboardingRequest createOnboardingRequestBody(OnboardingParameters parameters) {
    return this.getOnboardRequest(
        parameters.getUuid(),
        parameters.getApplicationId(),
        parameters.getCertificationVersionId(),
        parameters.getGatewayId(),
        parameters.getCertificationType());
  }

  private OnboardingResponse onboard(String registrationCode, OnboardingRequest onboardingRequest) {
    Response response =
        RequestFactory.bearerTokenRequest(this.environment.getOnboardUrl(), registrationCode)
            .post(Entity.json(onboardingRequest));
    this.assertResponseStatusIsValid(response, HttpStatus.SC_CREATED);
    return response.readEntity(OnboardingResponse.class);
  }

  @Override
  public String generateAuthenticationUrl(AuthenticationUrlParameters parameters) {
    return this.environment.getSecuredOnboardingAuthenticationUrl(
        parameters.getApplicationId(),
        parameters.getResponseType(),
        parameters.getState(),
        parameters.getRedirectUri());
  }
}
