package com.dke.data.agrirouter.documentation;

import com.dke.data.agrirouter.api.enums.ApplicationType;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.impl.onboard.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OnboardingForTelemetryConnectionTest extends AbstractIntegrationTest {

    @Test
    @Disabled("Please replace the placeholder for the registration code to run the test case.")
    void givenValidRegistrationCodeWhenOnboardingNewTelemetryConnectionThenTheNewEndpointShouldBeCreated() {

        // [1] Create onboarding parameters
        // ============================================================
        // Define the parameters used for onboarding, please be aware that the registration code needs
        // to be replaced with an actual code to run the test case.
        var onboardingParameters = new OnboardingParameters();
        onboardingParameters.setRegistrationCode("REPLACE_ME_WITH_ACTUAL_REGISTRATION_CODE");
        onboardingParameters.setApplicationId(communicationUnit.getApplicationId());
        onboardingParameters.setCertificationVersionId(communicationUnit.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setApplicationType(ApplicationType.APPLICATION);
        onboardingParameters.setUuid(UUID.randomUUID().toString());

        // [2] Onboard the new telemetry connection
        // ============================================================
        // Use the onboarding service to onboard the new telemetry connection.
        var onboardingService = new OnboardingServiceImpl(communicationUnit.getEnvironment());
        final var onboardingResponse = onboardingService.onboard(onboardingParameters);

        // [3] Validate the onboarding response
        // ============================================================
        // Check if the onboarding response contains all necessary information. This is just necessary within the tests, but not
        // needed in production code.
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
