package com.dke.data.agrirouter.test.messaging.rest;

import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.CloudOffboardingService;
import com.dke.data.agrirouter.api.service.messaging.http.CloudOnboardingService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.convenience.decode.DecodeCloudOnboardingResponsesService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.CloudOffboardingServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.CloudOnboardingServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.dke.data.agrirouter.test.OnboardingResponseRepository.Identifier;
import static com.dke.data.agrirouter.test.OnboardingResponseRepository.read;

class CloudOnboardingServiceTest extends AbstractIntegrationTest {

    public static final String EXTERNAL_ID = "8c31e156-3c29-4b46-863c-5e49b405b343";
    public static final String ENDPOINT_NAME = "CLOUD-ONBOARDING-SERVICE-TEST";

    /**
     * The endpoint ID for a former test run. If the endpoint is still available, then there could be
     * a chance to remove it. This has to be set manually.
     */
    public static final String VCU_ENDPOINT_ID_FOR_FORMER_TEST_RUN =
            "4e3af0ec-efb1-4109-8d3d-1910094c09d9";

    private OnboardingResponse virtualCommunicationUnit;

    @AfterEach
    void offboardVirtualCu() throws Throwable {
        offboardExistingVirtualCUFromFormerTestRun();

        CloudOffboardingService cloudOffboardingService = new CloudOffboardingServiceImpl();
        var parameters = new CloudOffboardingParameters();
        parameters.setEndpointIds(
                Collections.singletonList(virtualCommunicationUnit.getSensorAlternateId()));
        parameters.setOnboardingResponse(read(Identifier.TELEMETRY_PLATFORM));
        cloudOffboardingService.send(parameters);

        waitForTheAgrirouterToProcessSingleMessage();

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        read(Identifier.TELEMETRY_PLATFORM),
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(1, fetchMessageResponses.get().size());
        Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

        var command = fetchMessageResponses.get().get(0).getCommand();
        var message = command.getMessage();

        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodeMessageResponse = decodeMessageService.decode(message);

        Assertions.assertMatchesAny(
                Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
                decodeMessageResponse.getResponseEnvelope().getResponseCode());
    }

    private void offboardExistingVirtualCUFromFormerTestRun() throws Throwable {
        CloudOffboardingService cloudOffboardingService = new CloudOffboardingServiceImpl();
        var parameters = new CloudOffboardingParameters();
        parameters.setEndpointIds(Collections.singletonList(VCU_ENDPOINT_ID_FOR_FORMER_TEST_RUN));
        parameters.setOnboardingResponse(read(Identifier.TELEMETRY_PLATFORM));
        cloudOffboardingService.send(parameters);
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        read(Identifier.TELEMETRY_PLATFORM),
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
        waitForTheAgrirouterToProcessSingleMessage();
        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(1, fetchMessageResponses.get().size());
        Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());
    }

    @Test
    @Disabled(
            "Since there are multiple problems with the environment, the test is currently disabled.")
    void givenValidIdAndNameWhenOnboardingVirtualCuThenTheOnbardingShouldBePossible()
            throws Throwable {
        CloudOnboardingService cloudOnboardingService = new CloudOnboardingServiceImpl();
        var parameters = new CloudOnboardingParameters();
        var endpointDetails =
                new CloudOnboardingParameters.EndpointDetailsParameters();
        endpointDetails.setEndpointId(EXTERNAL_ID);
        endpointDetails.setEndpointName(ENDPOINT_NAME);
        parameters.setEndpointDetails(Collections.singletonList(endpointDetails));
        parameters.setOnboardingResponse(read(Identifier.TELEMETRY_PLATFORM));
        cloudOnboardingService.send(parameters);

        waitForTheAgrirouterToProcessSingleMessage();

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        read(Identifier.TELEMETRY_PLATFORM),
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(1, fetchMessageResponses.get().size());
        Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

        var command = fetchMessageResponses.get().get(0).getCommand();
        var message = command.getMessage();

        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodeMessageResponse = decodeMessageService.decode(message);

        Assertions.assertMatchesAny(
                Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
                decodeMessageResponse.getResponseEnvelope().getResponseCode());

        var decodeCloudOnboardingResponsesService =
                new DecodeCloudOnboardingResponsesService();
        var onboardingResponses =
                decodeCloudOnboardingResponsesService.decode(
                        fetchMessageResponses.get(), read(Identifier.TELEMETRY_PLATFORM));

        Assertions.assertEquals(1, onboardingResponses.size());

        virtualCommunicationUnit = onboardingResponses.get(0);

        Assertions.assertNotNull(virtualCommunicationUnit);
        Assertions.assertNotNull(virtualCommunicationUnit.getCapabilityAlternateId());
        Assertions.assertNotNull(virtualCommunicationUnit.getDeviceAlternateId());
        Assertions.assertNotNull(virtualCommunicationUnit.getSensorAlternateId());
        Assertions.assertNotNull(virtualCommunicationUnit.getAuthentication());
        Assertions.assertNotNull(virtualCommunicationUnit.getConnectionCriteria());
    }
}
