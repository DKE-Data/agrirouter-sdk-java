package com.dke.data.agrirouter.test.messaging.rest;

import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.SetCapabilityService;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SetCapabilityServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dke.data.agrirouter.test.OnboardingResponseRepository.Identifier;
import static com.dke.data.agrirouter.test.OnboardingResponseRepository.read;

/**
 * Demonstration how to use the service class.
 */
class SetCapabilityServiceTest extends AbstractIntegrationTest {

    @Test
    void givenValidEndpointWhenSendingCapabilitiesTheCapabilityMessageShouldBeAccepted()
            throws Throwable {
        SetCapabilityService setCapabilityService = new SetCapabilityServiceImpl();

        var parameters = new SetCapabilitiesParameters();
        parameters.setApplicationId(farmingSoftware.getApplicationId());
        parameters.setCertificationVersionId(farmingSoftware.getCertificationVersionId());
        parameters.setEnablePushNotifications(
                Capabilities.CapabilitySpecification.PushNotification.DISABLED);
        parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
        List<SetCapabilitiesParameters.CapabilityParameters> capabilities = new ArrayList<>();
        var capability =
                new SetCapabilitiesParameters.CapabilityParameters();
        capability.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        capability.setDirection(Capabilities.CapabilitySpecification.Direction.SEND_RECEIVE);
        capabilities.add(capability);
        parameters.setCapabilitiesParameters(capabilities);
        setCapabilityService.send(parameters);

        waitForTheAgrirouterToProcessSingleMessage();

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        read(Identifier.FARMING_SOFTWARE),
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

    @Test
    void givenValidEndpointWhenSendingInvalidCapabilitiesTheCapabilityMessageShouldNotBeAccepted()
            throws Throwable {
        SetCapabilityService setCapabilityService = new SetCapabilityServiceImpl();

        var parameters = new SetCapabilitiesParameters();
        parameters.setApplicationId(farmingSoftware.getApplicationId());
        parameters.setCertificationVersionId(farmingSoftware.getCertificationVersionId());
        parameters.setEnablePushNotifications(
                Capabilities.CapabilitySpecification.PushNotification.DISABLED);
        parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
        List<SetCapabilitiesParameters.CapabilityParameters> capabilities = new ArrayList<>();
        var capability =
                new SetCapabilitiesParameters.CapabilityParameters();
        capability.setTechnicalMessageType(SystemMessageType.DKE_FEED_DELETE);
        capability.setDirection(Capabilities.CapabilitySpecification.Direction.SEND_RECEIVE);
        capabilities.add(capability);
        parameters.setCapabilitiesParameters(capabilities);
        setCapabilityService.send(parameters);

        waitForTheAgrirouterToProcessSingleMessage();

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        read(Identifier.FARMING_SOFTWARE),
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(1, fetchMessageResponses.get().size());
        Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

        var command = fetchMessageResponses.get().get(0).getCommand();
        var encodedMessage = command.getMessage();

        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodeMessageResponse = decodeMessageService.decode(encodedMessage);

        Assertions.assertEquals(
                HttpStatus.SC_BAD_REQUEST, decodeMessageResponse.getResponseEnvelope().getResponseCode());

        var messages =
                decodeMessageService.decode(decodeMessageResponse.getResponsePayloadWrapper().getDetails());
        Assertions.assertEquals(1, messages.getMessagesCount());

        var message = messages.getMessages(0);
        Assertions.assertEquals("VAL_000007", message.getMessageCode());
    }
}
