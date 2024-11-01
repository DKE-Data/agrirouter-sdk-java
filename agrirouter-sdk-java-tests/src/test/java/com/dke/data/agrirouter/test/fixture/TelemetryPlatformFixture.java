package com.dke.data.agrirouter.test.fixture;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.onboard.secured.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.SecuredOnboardingParameters;
import com.dke.data.agrirouter.impl.onboard.secured.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class to onboard endpoints for different reasons.
 */
@SuppressWarnings("ALL")
class TelemetryPlatformFixture extends AbstractIntegrationTest {

    /**
     * Create a new registration token by using the following link:
     *
     * <p>https://agrirouter-qa.cfapps.eu10.hana.ondemand.com/application/3c3559c9-7062-4628-a4f7-c9f5aa07265f/authorize?response_type=onboard&state=my-custom-state-to-identify-the-request&redirect_uri=
     */
    @Test
    @Disabled("Please replace the placeholder for the registration code to run the test case.")
    void onboardTelemetryPlatformAndSaveToFile() throws IOException {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        SecuredOnboardingParameters onboardingParameters = new SecuredOnboardingParameters();
        onboardingParameters.setRegistrationCode("8530975704");
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
        OnboardingResponseRepository.save(
                OnboardingResponseRepository.Identifier.TELEMETRY_PLATFORM, onboardingResponse);
    }

}
