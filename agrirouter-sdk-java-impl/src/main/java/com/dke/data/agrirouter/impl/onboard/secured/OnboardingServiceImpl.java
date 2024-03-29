package com.dke.data.agrirouter.impl.onboard.secured;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.CouldNotVerifySecuredOnboardingRequestException;
import com.dke.data.agrirouter.api.exception.InvalidSignatureException;
import com.dke.data.agrirouter.api.exception.OnboardingException;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import com.dke.data.agrirouter.api.service.onboard.secured.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.SecuredOnboardingParameters;
import com.dke.data.agrirouter.impl.RequestFactory;
import com.dke.data.agrirouter.impl.common.signing.SecurityKeyCreationService;
import com.dke.data.agrirouter.impl.onboard.AbstractOnboardingService;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Internal service implementation.
 */
public class OnboardingServiceImpl extends AbstractOnboardingService
        implements OnboardingService, ResponseValidator {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

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

    @Override
    public Optional<OnboardingError> getLastOnboardingError(String error) {
        return getOnboardingError(error);
    }

    private OnboardingRequest createOnboardingRequestBody(SecuredOnboardingParameters parameters) {
        return this.getOnboardRequest(
                parameters.getUuid(),
                parameters.getApplicationId(),
                parameters.getCertificationVersionId(),
                parameters.getGatewayId(),
                Objects.requireNonNull(parameters.getCertificationType()));
    }

    private OnboardingResponse onboard(
            SecuredOnboardingParameters securedOnboardingParameters,
            OnboardingRequest onboardingRequest) {
        var jsonBody = new Gson().toJson(onboardingRequest).replace("\n", "");
        var encodedSignature = this.createSignature(securedOnboardingParameters, jsonBody);
        this.verifySignature(
                jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
        var response =
                RequestFactory.bearerTokenRequest(
                                this.environment.getSecuredOnboardUrl(),
                                securedOnboardingParameters.getRegistrationCode(),
                                securedOnboardingParameters.getApplicationId(),
                                encodedSignature)
                        .post(Entity.entity(jsonBody, MediaType.APPLICATION_JSON_TYPE));
        try {
            response.bufferEntity();
            this.assertStatusCodeIsCreated(response.getStatus());
            return response.readEntity(OnboardingResponse.class);
        } catch (Exception e) {
            throw new OnboardingException(getOnboardingError(response.readEntity(String.class)));
        } finally {
            response.close();
        }
    }

    @SuppressWarnings("resource")
    private void verify(
            SecuredOnboardingParameters securedOnboardingParameters,
            OnboardingRequest onboardingRequest) {
        var jsonBody = new Gson().toJson(onboardingRequest).replace("\n", "");
        var encodedSignature = this.createSignature(securedOnboardingParameters, jsonBody);
        this.verifySignature(
                jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
        System.out.println(
                "Validation of '" + jsonBody + "' against '" + encodedSignature + "' was successful.");
        var response =
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
        var signature = this.createSignature(jsonBody, securedOnboardingParameters.getPrivateKey());
        this.verifySignature(jsonBody, signature, securedOnboardingParameters.getPublicKey());
        var encodedSignature = Hex.encodeHexString(signature);
        this.verifySignature(
                jsonBody, decodeHex(encodedSignature), securedOnboardingParameters.getPublicKey());
        return encodedSignature;
    }

    byte[] createSignature(String requestBody, String privateKey) {
        try {
            var securityKeyCreationService = new SecurityKeyCreationService();
            var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(securityKeyCreationService.createPrivateKey(privateKey));
            signature.update(requestBody.getBytes(UTF_8));
            return signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    void verifySignature(String requestBody, byte[] signedBytes, String publicKey) {
        try {
            var securityKeyCreationService = new SecurityKeyCreationService();
            var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(securityKeyCreationService.createPublicKey(publicKey));
            signature.update(requestBody.getBytes(UTF_8));
            if (!signature.verify(signedBytes)) {
                throw new InvalidSignatureException();
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
