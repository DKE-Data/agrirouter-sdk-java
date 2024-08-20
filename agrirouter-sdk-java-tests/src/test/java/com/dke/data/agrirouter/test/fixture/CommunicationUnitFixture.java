package com.dke.data.agrirouter.test.fixture;

import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.ApplicationType;
import com.dke.data.agrirouter.api.enums.CertificationType;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.Gateway;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.onboard.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.OnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SetCapabilityServiceImpl;
import com.dke.data.agrirouter.impl.onboard.OnboardingServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class to onboard endpoints for different reasons.
 */
@SuppressWarnings("ALL")
class CommunicationUnitFixture extends AbstractIntegrationTest {

    /**
     * Create a new registration token by using the agrirouter UI and select the integration test
     * application for CUs.
     */
    @Test
    @Disabled("Please replace the placeholder for the registration code to run the test case.")
    void onboardCommunicationUnitAndSaveToFile() throws IOException {
        OnboardingService onboardingService = new OnboardingServiceImpl(new QA() {
        });
        OnboardingParameters onboardingParameters = new OnboardingParameters();
        onboardingParameters.setRegistrationCode("8908462691");
        onboardingParameters.setApplicationId(communicationUnit.getApplicationId());
        onboardingParameters.setCertificationVersionId(communicationUnit.getCertificationVersionId());
        onboardingParameters.setCertificationType(CertificationType.P12);
        onboardingParameters.setGatewayId(Gateway.REST.getKey());
        onboardingParameters.setApplicationType(ApplicationType.APPLICATION);
        onboardingParameters.setUuid(UUID.randomUUID().toString());
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
                OnboardingResponseRepository.Identifier.COMMUNICATION_UNIT, onboardingResponse);
        final SetCapabilityServiceImpl setCapabilityService = new SetCapabilityServiceImpl();
        final SetCapabilitiesParameters setCapabilitiesParameters = new SetCapabilitiesParameters();
        setCapabilitiesParameters.setApplicationId(communicationUnit.getApplicationId());
        setCapabilitiesParameters.setCertificationVersionId(
                communicationUnit.getCertificationVersionId());
        setCapabilitiesParameters.setOnboardingResponse(onboardingResponse);
        final SetCapabilitiesParameters.CapabilityParameters capabilityParameters =
                new SetCapabilitiesParameters.CapabilityParameters();
        capabilityParameters.setDirection(Capabilities.CapabilitySpecification.Direction.SEND_RECEIVE);
        capabilityParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        setCapabilitiesParameters.setCapabilitiesParameters(
                Collections.singletonList(capabilityParameters));
        setCapabilityService.send(setCapabilitiesParameters);
        final FetchMessageServiceImpl fetchMessageService = new FetchMessageServiceImpl();
        fetchMessageService.fetch(
                onboardingResponse,
                new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
    }
}
