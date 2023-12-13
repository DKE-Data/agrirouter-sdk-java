package com.dke.data.agrirouter.test.messaging.rest;

import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.exception.UnauthorizedRequestException;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Class to show how health checking via polling can be implemented.
 */
class HealthCheckForEndpointsByPollingTest extends AbstractIntegrationTest {

    @Test
    void
    givenExistingAndActivatedEndpointWhenPollingTheOutboxThenTheAgrirouterShouldReturnAnEmptyResult()
            throws IOException {
        final OnboardingResponse onboardingResponse =
                OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        final Optional<List<FetchMessageResponse>> optionalMessageResponses =
                fetchMessageService.fetch(
                        onboardingResponse,
                        new DefaultCancellationToken(
                                MessageFetcher.MAX_TRIES_BEFORE_FAILURE, MessageFetcher.DEFAULT_INTERVAL));
        Assertions.assertFalse(optionalMessageResponses.isPresent());
    }

    @Test
    void
    givenExistingAndDeactivatedEndpointWhenPollingTheOutboxThenTheAgrirouterShouldReturnAnEmptyResult()
            throws IOException {
        final OnboardingResponse onboardingResponse =
                OnboardingResponseRepository.read(
                        OnboardingResponseRepository.Identifier.FARMING_SOFTWARE_DEACTIVATED);
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        final Optional<List<FetchMessageResponse>> optionalMessageResponses =
                fetchMessageService.fetch(
                        onboardingResponse,
                        new DefaultCancellationToken(
                                MessageFetcher.MAX_TRIES_BEFORE_FAILURE, MessageFetcher.DEFAULT_INTERVAL));
        Assertions.assertFalse(optionalMessageResponses.isPresent());
    }

    @Test
    void givenRemovedEndpointWhenPollingTheOutboxThenTheAgrirouterShouldDenyAccessToTheApi()
            throws IOException {
        final OnboardingResponse onboardingResponse =
                OnboardingResponseRepository.read(
                        OnboardingResponseRepository.Identifier.FARMING_SOFTWARE_REMOVED);
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        Assertions.assertThrows(
                UnauthorizedRequestException.class,
                () ->
                        fetchMessageService.fetch(
                                onboardingResponse,
                                new DefaultCancellationToken(
                                        MessageFetcher.MAX_TRIES_BEFORE_FAILURE, MessageFetcher.DEFAULT_INTERVAL)));
    }
}
