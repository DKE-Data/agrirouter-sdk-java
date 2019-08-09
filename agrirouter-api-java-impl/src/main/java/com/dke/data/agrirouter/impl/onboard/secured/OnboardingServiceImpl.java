package com.dke.data.agrirouter.impl.onboard.secured;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotVerifySecuredOnboardingRequestException;
import com.dke.data.agrirouter.api.exception.InvalidSignatureException;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import com.dke.data.agrirouter.api.service.onboard.secured.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.AuthorizationRequestParameters;
import com.dke.data.agrirouter.api.service.parameters.SecuredOnboardingParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.signing.SecurityKeyCreationService;
import com.dke.data.agrirouter.impl.onboard.AbstractOnboardingService;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.gson.Gson;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Optional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/** Internal service implementation. */
public class OnboardingServiceImpl extends AbstractOnboardingService
    implements OnboardingService, ResponseValidator {

  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
  private String lastError;

  public OnboardingServiceImpl(Environment environment) {
    super(environment);
  }

  @Override
  public OnboardingResponse onboard(SecuredOnboardingParameters securedOnboardingParameters) {
    securedOnboardingParameters.validate();
    return this.onboard(
        securedOnboardingParameters, this.createOnboardingRequestBody(securedOnboardingParameters));
  }

  @Override
  public void verify(SecuredOnboardingParameters securedOnboardingParameters) {
    securedOnboardingParameters.validate();
    this.verify(
        securedOnboardingParameters, this.createOnboardingRequestBody(securedOnboardingParameters));
  }

  private OnboardingRequest createOnboardingRequestBody(SecuredOnboardingParameters parameters) {
    return this.getOnboardRequest(
        parameters.getUuid(),
        parameters.getApplicationId(),
        parameters.getCertificationVersionId(),
        parameters.getGatewayId(),
        parameters.getCertificationType());
  }

  private OnboardingResponse onboard(
      SecuredOnboardingParameters securedOnboardingParameters,
      OnboardingRequest onboardingRequest) {
    this.lastError = "";
    String jsonBody = new Gson().toJson(onboardingRequest).replace("\n", "");
    String encodedSignature = this.createSignature(securedOnboardingParameters, jsonBody);
    this.verifySignature(
        jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
    Response response =
        RequestFactory.bearerTokenRequest(
                this.environment.getSecuredOnboardUrl(),
                securedOnboardingParameters.getRegistrationCode(),
                securedOnboardingParameters.getApplicationId(),
                encodedSignature)
            .post(Entity.entity(jsonBody, MediaType.APPLICATION_JSON_TYPE));
    try {
      response.bufferEntity();
      this.lastError = response.readEntity(String.class);
      this.assertStatusCodeIsCreated(response.getStatus());
      this.lastError = "";
      return response.readEntity(OnboardingResponse.class);
    } finally {
      response.close();
    }
  }

  private void verify(
      SecuredOnboardingParameters securedOnboardingParameters,
      OnboardingRequest onboardingRequest) {
    String jsonBody = new Gson().toJson(onboardingRequest).replace("\n", "");
    String encodedSignature = this.createSignature(securedOnboardingParameters, jsonBody);
    this.verifySignature(
        jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
    System.out.println(
        "Validation of '" + jsonBody + "' against '" + encodedSignature + "' was successful.");
    Response response =
        RequestFactory.bearerTokenRequest(
                this.environment.getVerifyOnboardRequestUrl(),
                securedOnboardingParameters.getRegistrationCode(),
                securedOnboardingParameters.getApplicationId(),
                encodedSignature)
            .post(Entity.entity(jsonBody, MediaType.APPLICATION_JSON_TYPE));
    try {
      this.assertStatusCodeIsOk(response.getStatus());
    } catch (UnexpectedHttpStatusException e) {
      throw new CouldNotVerifySecuredOnboardingRequestException(e);
    }
  }

  private byte[] decodeHex(String encodedSignature) {
    try {
      return Hex.decodeHex(encodedSignature.toCharArray());
    } catch (DecoderException e) {
      throw new RuntimeException(e);
    }
  }

  private String createSignature(
      SecuredOnboardingParameters securedOnboardingParameters, String jsonBody) {
    byte[] signature = this.createSignature(jsonBody, securedOnboardingParameters.getPrivateKey());
    this.verifySignature(jsonBody, signature, securedOnboardingParameters.getPublicKey());
    String encodedSignature = Hex.encodeHexString(signature);
    this.verifySignature(
        jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
    return encodedSignature;
  }

  byte[] createSignature(String requestBody, String privateKey) {
    try {
      SecurityKeyCreationService securityKeyCreationService = new SecurityKeyCreationService();
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initSign(securityKeyCreationService.createPrivateKey(privateKey));
      signature.update(requestBody.getBytes(UTF_8));
      return signature.sign();
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  void verifySignature(String requestBody, byte[] signedBytes, String publicKey) {
    try {
      SecurityKeyCreationService securityKeyCreationService = new SecurityKeyCreationService();
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initVerify(securityKeyCreationService.createPublicKey(publicKey));
      signature.update(requestBody.getBytes(UTF_8));
      if (!signature.verify(signedBytes)) {
        throw new InvalidSignatureException();
      }
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String generateAuthorizationUrl(AuthorizationRequestParameters parameters) {
    return this.environment.getSecuredOnboardingAuthorizationUrl(
        parameters.getApplicationId(),
        parameters.getResponseType(),
        parameters.getState(),
        parameters.getRedirectUri());
  }

  @Override
  public String getLastErrorAsString() {
    return this.lastError;
  }

  @Override
  public Optional<OnboardingError> getLastError() {
    if (this.lastError == null || this.lastError.equals("")) {
      return Optional.empty();

    } else {
      Gson gson = new Gson();
      return Optional.of(gson.fromJson(this.lastError, OnboardingError.class));
    }
  }
}
