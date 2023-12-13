package com.dke.data.agrirouter.test.messaging.rest;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.SetSubscriptionService;
import com.dke.data.agrirouter.api.service.parameters.SetSubscriptionParameters;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SetSubscriptionServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.dke.data.agrirouter.test.OnboardingResponseRepository.Identifier;
import static com.dke.data.agrirouter.test.OnboardingResponseRepository.read;

/**
 * Demonstration how to use the service class.
 */
class SetSubscriptionServiceTest extends AbstractIntegrationTest {

    @Test
    void givenValidEndpointWhenSendingSubscriptionsTheSubscriptionMessageShouldBeAccepted()
            throws Throwable {
        SetSubscriptionService setSubscriptionService = new SetSubscriptionServiceImpl(new QA() {
        });

        var parameters = new SetSubscriptionParameters();
        parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
        List<SetSubscriptionParameters.Subscription> subscriptions = new ArrayList<>();
        var subscription =
                new SetSubscriptionParameters.Subscription();
        subscription.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        subscriptions.add(subscription);
        parameters.setSubscriptions(subscriptions);
        setSubscriptionService.send(parameters);

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
    void givenValidEndpointWhenSendingInvalidSubscriptionsTheSubscriptionMessageShouldNotBeAccepted()
            throws Throwable {
        SetSubscriptionService setSubscriptionService = new SetSubscriptionServiceImpl(new QA() {
        });

        var parameters = new SetSubscriptionParameters();
        parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
        List<SetSubscriptionParameters.Subscription> subscriptions = new ArrayList<>();
        var subscription =
                new SetSubscriptionParameters.Subscription();
        subscription.setTechnicalMessageType(SystemMessageType.DKE_FEED_DELETE);
        subscriptions.add(subscription);
        parameters.setSubscriptions(subscriptions);
        setSubscriptionService.send(parameters);

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
        Assertions.assertEquals("VAL_000006", message.getMessageCode());
    }
}
