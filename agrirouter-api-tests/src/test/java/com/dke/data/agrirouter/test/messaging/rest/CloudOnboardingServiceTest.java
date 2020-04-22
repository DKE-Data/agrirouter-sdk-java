package com.dke.data.agrirouter.test.messaging.rest;

import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.messaging.CloudOffboardingService;
import com.dke.data.agrirouter.api.service.messaging.CloudOnboardingService;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.convenience.decode.DecodeCloudOnboardingResponsesService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.CloudOffboardingServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.CloudOnboardingServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CloudOnboardingServiceTest extends AbstractIntegrationTest {

  public static final String EXTERNAL_ID = "8c31e156-3c29-4b46-863c-5e49b405b343";
  public static final String ENDPOINT_NAME = "CLOUD-ONBOARDING-SERVICE-TEST";

  private OnboardingResponse virtualCommunicationUnit;

  @AfterEach
  void offboardVirtualCu() throws Throwable {
    CloudOffboardingService cloudOffboardingService = new CloudOffboardingServiceImpl();
    CloudOffboardingParameters parameters = new CloudOffboardingParameters();
    parameters.setEndpointIds(
        Collections.singletonList(virtualCommunicationUnit.getSensorAlternateId()));
    parameters.setOnboardingResponse(getOnboardingResponse());
    cloudOffboardingService.send(parameters);

    Thread.sleep(TimeUnit.SECONDS.toMillis(5));

    FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        fetchMessageService.fetch(
            getOnboardingResponse(),
            MessageFetcher.MAX_TRIES_BEFORE_FAILURE,
            MessageFetcher.DEFAULT_INTERVAL);

    Assertions.assertTrue(fetchMessageResponses.isPresent());
    Assertions.assertEquals(1, fetchMessageResponses.get().size());
    Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

    Message command = fetchMessageResponses.get().get(0).getCommand();
    String message = command.getMessage();

    DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
    DecodeMessageResponse decodeMessageResponse = decodeMessageService.decode(message);

    Assertions.assertMatchesAny(
        Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
        decodeMessageResponse.getResponseEnvelope().getResponseCode());
  }

  @Test
  void givenValidIdAndNameWhenOnboardingVirtualCuThenTheOnbardingShouldBePossible()
      throws Throwable {
    CloudOnboardingService cloudOnboardingService = new CloudOnboardingServiceImpl();
    CloudOnboardingParameters parameters = new CloudOnboardingParameters();
    CloudOnboardingParameters.EndpointDetailsParameters endpointDetails =
        new CloudOnboardingParameters.EndpointDetailsParameters();
    endpointDetails.setEndpointId(EXTERNAL_ID);
    endpointDetails.setEndpointName(ENDPOINT_NAME);
    parameters.setEndpointDetails(Collections.singletonList(endpointDetails));
    parameters.setOnboardingResponse(getOnboardingResponse());
    cloudOnboardingService.send(parameters);

    Thread.sleep(TimeUnit.SECONDS.toMillis(5));

    FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        fetchMessageService.fetch(
            getOnboardingResponse(),
            MessageFetcher.MAX_TRIES_BEFORE_FAILURE,
            MessageFetcher.DEFAULT_INTERVAL);

    Assertions.assertTrue(fetchMessageResponses.isPresent());
    Assertions.assertEquals(1, fetchMessageResponses.get().size());
    Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

    Message command = fetchMessageResponses.get().get(0).getCommand();
    String message = command.getMessage();

    DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
    DecodeMessageResponse decodeMessageResponse = decodeMessageService.decode(message);

    Assertions.assertMatchesAny(
        Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
        decodeMessageResponse.getResponseEnvelope().getResponseCode());

    DecodeCloudOnboardingResponsesService decodeCloudOnboardingResponsesService =
        new DecodeCloudOnboardingResponsesService();
    List<OnboardingResponse> onboardingResponses =
        decodeCloudOnboardingResponsesService.decode(
            fetchMessageResponses.get(), getOnboardingResponse());

    Assertions.assertEquals(1, onboardingResponses.size());

    virtualCommunicationUnit = onboardingResponses.get(0);

    Assertions.assertNotNull(virtualCommunicationUnit);
    Assertions.assertNotNull(virtualCommunicationUnit.getCapabilityAlternateId());
    Assertions.assertNotNull(virtualCommunicationUnit.getDeviceAlternateId());
    Assertions.assertNotNull(virtualCommunicationUnit.getSensorAlternateId());
    Assertions.assertNotNull(virtualCommunicationUnit.getAuthentication());
    Assertions.assertNotNull(virtualCommunicationUnit.getConnectionCriteria());
  }

  private OnboardingResponse getOnboardingResponse() {
    String onboardingResponseAsJson =
        "{\"deviceAlternateId\":\"4b6c4c92-806f-4295-b19b-c5076b824661\",\"capabilityAlternateId\":\"c2467f6d-0a7e-48ca-9b57-1862186aef12\",\"sensorAlternateId\":\"0e1607ed-57cc-4ce3-bdb9-2cf465b61e84\",\"connectionCriteria\":{\"gatewayId\":\"3\",\"measures\":\"https://dke-qa.eu10.cp.iot.sap/iot/gateway/rest/measures/4b6c4c92-806f-4295-b19b-c5076b824661\",\"commands\":\"https://dke-qa.eu10.cp.iot.sap/iot/gateway/rest/commands/4b6c4c92-806f-4295-b19b-c5076b824661\"},\"authentication\":{\"type\":\"P12\",\"secret\":\"9tbtNSpeZnn6REJvB2j5oWywLlguuS1HidIt\",\"certificate\":\"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\"}}";
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(onboardingResponseAsJson, OnboardingResponse.class);
  }
}
