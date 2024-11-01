package com.dke.data.agrirouter.test.messaging.rest;

import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Class to show how health checking via polling can be implemented.
 */
class HealthCheckForEndpointsByPollingTest extends AbstractIntegrationTest {

    @Test
    void
    givenExistingAndActivatedEndpointWhenPollingTheOutboxThenTheAgrirouterShouldReturnAnEmptyResult()
            throws IOException {
        final var onboardingResponse =
                OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        final var optionalMessageResponses =
                fetchMessageService.fetch(
                        onboardingResponse,
                        new DefaultCancellationToken(
                                MessageFetcher.MAX_TRIES_BEFORE_FAILURE, MessageFetcher.DEFAULT_INTERVAL));
        Assertions.assertFalse(optionalMessageResponses.isPresent());
    }

}
