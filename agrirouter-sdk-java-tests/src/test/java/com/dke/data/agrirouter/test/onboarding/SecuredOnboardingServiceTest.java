package com.dke.data.agrirouter.test.onboarding;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.exception.OnboardingException;
import com.dke.data.agrirouter.api.service.onboard.secured.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.SecuredOnboardingParameters;
import com.dke.data.agrirouter.impl.onboard.secured.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Demonstration how to use the service for onboarding.
 */
class SecuredOnboardingServiceTest extends AbstractIntegrationTest {

    @Test
    void
    givenInvalidRegistrationCodeThereShouldBeAnOnboardingExceptionWhenSendingAnOnboardingRequest() {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        SecuredOnboardingParameters onboardingParameters = new SecuredOnboardingParameters();
        onboardingParameters.setRegistrationCode("SOME_INVALID_REGISTRATION_CODE");
        onboardingParameters.setApplicationId(farmingSoftware.getApplicationId());
        onboardingParameters.setCertificationVersionId(farmingSoftware.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setUuid(UUID.randomUUID().toString());
        onboardingParameters.setPrivateKey(farmingSoftware.getPrivateKey());
        onboardingParameters.setPublicKey(farmingSoftware.getPublicKey());
        assertThrows(OnboardingException.class, () -> onboardingService.onboard(onboardingParameters));
    }

    @Test
    @Disabled("Please replace the placeholder for the registration code to run the test case.")
    void
    givenValidRegistrationCodeThereShouldBeAnOnboardingResponseWhenSendingAnOnboardingRequestForFarmingSoftware() {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        SecuredOnboardingParameters onboardingParameters = new SecuredOnboardingParameters();
        onboardingParameters.setRegistrationCode("PLEASE_REPLACE_ME");
        onboardingParameters.setApplicationId(farmingSoftware.getApplicationId());
        onboardingParameters.setCertificationVersionId(farmingSoftware.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setUuid(UUID.randomUUID().toString());
        onboardingParameters.setPrivateKey(farmingSoftware.getPrivateKey());
        onboardingParameters.setPublicKey(farmingSoftware.getPublicKey());
        final OnboardingResponse onboardingResponse = onboardingService.onboard(onboardingParameters);
        assertNotNull(onboardingResponse);
        assertNotNull(onboardingResponse.getCapabilityAlternateId());
        assertNotNull(onboardingResponse.getDeviceAlternateId());
        assertNotNull(onboardingResponse.getSensorAlternateId());
        assertNotNull(onboardingResponse.getAuthentication());
        assertNotNull(onboardingResponse.getAuthentication().getCertificate());
        assertNotNull(onboardingResponse.getAuthentication().getSecret());
        assertNotNull(onboardingResponse.getAuthentication().getType());
        assertNotNull(onboardingResponse.getConnectionCriteria());
        assertNotNull(onboardingResponse.getConnectionCriteria().getMeasures());
        assertNotNull(onboardingResponse.getConnectionCriteria().getCommands());
    }

    @Test
    @Disabled("Please replace the placeholder for the registration code to run the test case.")
    void
    givenValidRegistrationCodeThereShouldBeAnOnboardingResponseWhenSendingAnOnboardingRequestForTelemetryPlatform() {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        SecuredOnboardingParameters onboardingParameters = new SecuredOnboardingParameters();
        onboardingParameters.setRegistrationCode("PLEASE_REPLACE_ME");
        onboardingParameters.setApplicationId(telemetryPlatform.getApplicationId());
        onboardingParameters.setCertificationVersionId(telemetryPlatform.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setUuid(UUID.randomUUID().toString());
        onboardingParameters.setPrivateKey(telemetryPlatform.getPrivateKey());
        onboardingParameters.setPublicKey(telemetryPlatform.getPublicKey());
        final OnboardingResponse onboardingResponse = onboardingService.onboard(onboardingParameters);
        assertNotNull(onboardingResponse);
        assertNotNull(onboardingResponse.getCapabilityAlternateId());
        assertNotNull(onboardingResponse.getDeviceAlternateId());
        assertNotNull(onboardingResponse.getSensorAlternateId());
        assertNotNull(onboardingResponse.getAuthentication());
        assertNotNull(onboardingResponse.getAuthentication().getCertificate());
        assertNotNull(onboardingResponse.getAuthentication().getSecret());
        assertNotNull(onboardingResponse.getAuthentication().getType());
        assertNotNull(onboardingResponse.getConnectionCriteria());
        assertNotNull(onboardingResponse.getConnectionCriteria().getMeasures());
        assertNotNull(onboardingResponse.getConnectionCriteria().getCommands());
    }
}
