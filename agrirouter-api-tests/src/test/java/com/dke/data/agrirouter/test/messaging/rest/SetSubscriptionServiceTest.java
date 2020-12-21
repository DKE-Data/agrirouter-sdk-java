package com.dke.data.agrirouter.test.messaging.rest;

import static com.dke.data.agrirouter.test.OnboardingResponseRepository.*;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.SetSubscriptionService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.SetSubscriptionParameters;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.dke.data.agrirouter.impl.messaging.rest.SetSubscriptionServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

/** Demonstration how to use the service class. */
class SetSubscriptionServiceTest extends AbstractIntegrationTest {

  @Test
  void givenValidEndpointWhenSendingSubscriptionsTheSubscriptionMessageShouldBeAccepted()
      throws Throwable {
    SetSubscriptionService setSubscriptionService = new SetSubscriptionServiceImpl(new QA() {});

    SetSubscriptionParameters parameters = new SetSubscriptionParameters();
    parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
    List<SetSubscriptionParameters.Subscription> subscriptions = new ArrayList<>();
    SetSubscriptionParameters.Subscription subscription =
        new SetSubscriptionParameters.Subscription();
    subscription.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    subscriptions.add(subscription);
    parameters.setSubscriptions(subscriptions);
    setSubscriptionService.send(parameters);

    Thread.sleep(TimeUnit.SECONDS.toMillis(3));

    FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        fetchMessageService.fetch(
            read(Identifier.FARMING_SOFTWARE),
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
  void givenValidEndpointWhenSendingInvalidSubscriptionsTheSubscriptionMessageShouldNotBeAccepted()
      throws Throwable {
    SetSubscriptionService setSubscriptionService = new SetSubscriptionServiceImpl(new QA() {});

    SetSubscriptionParameters parameters = new SetSubscriptionParameters();
    parameters.setOnboardingResponse(read(Identifier.FARMING_SOFTWARE));
    List<SetSubscriptionParameters.Subscription> subscriptions = new ArrayList<>();
    SetSubscriptionParameters.Subscription subscription =
        new SetSubscriptionParameters.Subscription();
    subscription.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_DELETE);
    subscriptions.add(subscription);
    parameters.setSubscriptions(subscriptions);
    setSubscriptionService.send(parameters);

    Thread.sleep(TimeUnit.SECONDS.toMillis(3));

    FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        fetchMessageService.fetch(
            read(Identifier.FARMING_SOFTWARE),
            MessageFetcher.MAX_TRIES_BEFORE_FAILURE,
            MessageFetcher.DEFAULT_INTERVAL);

    Assertions.assertTrue(fetchMessageResponses.isPresent());
    Assertions.assertEquals(1, fetchMessageResponses.get().size());
    Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());

    Message command = fetchMessageResponses.get().get(0).getCommand();
    String encodedMessage = command.getMessage();

    DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
    DecodeMessageResponse decodeMessageResponse = decodeMessageService.decode(encodedMessage);

    Assertions.assertEquals(
        HttpStatus.SC_BAD_REQUEST, decodeMessageResponse.getResponseEnvelope().getResponseCode());

    MessageOuterClass.Messages messages =
        decodeMessageService.decode(decodeMessageResponse.getResponsePayloadWrapper().getDetails());
    Assertions.assertEquals(1, messages.getMessagesCount());

    MessageOuterClass.Message message = messages.getMessages(0);
    Assertions.assertEquals("VAL_000006", message.getMessageCode());
  }
}
