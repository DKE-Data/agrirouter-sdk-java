package com.dke.data.agrirouter.convenience.rest.service.messaging;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.feed.response.FeedResponse;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.cancellation.CancellationToken;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.MessageConfirmationService;
import com.dke.data.agrirouter.api.service.messaging.http.MessageQueryService;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationForAllPendingMessagesParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageConfirmationParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageConfirmationServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageQueryServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Convenience implementation for message confirmation. This service contains common functions to
 * confirm dedicated messages.
 */
public class MessageConfirmationFunctionsService implements ResponseValidator {

  private final MessageQueryService messageQueryService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;
  private final MessageConfirmationService messageConfirmationService;

  public MessageConfirmationFunctionsService(Environment environment) {
    this.messageQueryService = new MessageQueryServiceImpl(environment);
    this.fetchMessageService = new FetchMessageServiceImpl();
    this.decodeMessageService = new DecodeMessageServiceImpl();
    this.messageConfirmationService = new MessageConfirmationServiceImpl(environment);
  }

  public void confirmAllPendingMessagesWithValidation(
      MessageConfirmationForAllPendingMessagesParameters parameters) {
    this.confirmAllPendingMessages(
        parameters, true, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
  }

  public void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters) {
    this.confirmAllPendingMessages(
        parameters,
        false,
        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
  }

  public void confirmAllPendingMessagesWithValidation(
      MessageConfirmationForAllPendingMessagesParameters parameters,
      CancellationToken cancellationToken) {
    this.confirmAllPendingMessages(parameters, true, cancellationToken);
  }

  public void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters,
      CancellationToken cancellationToken) {
    this.confirmAllPendingMessages(parameters, false, cancellationToken);
  }

  private void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters,
      boolean enableValidation,
      CancellationToken cancellationToken) {
    MessageQueryParameters messageQueryParameters = new MessageQueryParameters();
    messageQueryParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    messageQueryParameters.setMessageIds(Collections.emptyList());
    messageQueryParameters.setSenderIds(Collections.emptyList());
    messageQueryParameters.setSentFromInSeconds(
        UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
    messageQueryParameters.setSentToInSeconds(UtcTimeService.now().toEpochSecond());

    this.messageQueryService.send(messageQueryParameters);

    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        this.fetchMessageService.fetch(parameters.getOnboardingResponse(), cancellationToken);
    if (fetchMessageResponses.isPresent()) {
      DecodeMessageResponse decodedMessageQueryResponse =
          this.decodeMessageService.decode(
              fetchMessageResponses.get().get(0).getCommand().getMessage());
      if (decodedMessageQueryResponse.getResponseEnvelope().getType()
              == Response.ResponseEnvelope.ResponseBodyType.ACK_FOR_FEED_MESSAGE
          && this.assertStatusCodeIsValid(
              decodedMessageQueryResponse.getResponseEnvelope().getResponseCode())) {
        FeedResponse.MessageQueryResponse messageQueryResponse =
            this.messageQueryService.decode(
                decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails().getValue());
        List<String> messageIds = new ArrayList<>();
        messageQueryResponse
            .getMessagesList()
            .forEach(feedMessage -> messageIds.add(feedMessage.getHeader().getMessageId()));
        MessageConfirmationParameters messageConfirmationParameters =
            new MessageConfirmationParameters();
        messageConfirmationParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        messageConfirmationParameters.setMessageIds(messageIds);
        this.messageConfirmationService.send(messageConfirmationParameters);
        if (enableValidation) {
          this.validateResponse(parameters, cancellationToken);
        }
      }
    }
  }

  private void validateResponse(
      MessageConfirmationForAllPendingMessagesParameters parameters,
      CancellationToken cancellationToken) {
    Optional<List<FetchMessageResponse>> fetchMessageResponses;
    DecodeMessageResponse decodedMessageQueryResponse;
    fetchMessageResponses =
        this.fetchMessageService.fetch(parameters.getOnboardingResponse(), cancellationToken);
    if (fetchMessageResponses.isPresent()) {
      decodedMessageQueryResponse =
          this.decodeMessageService.decode(
              fetchMessageResponses.get().get(0).getCommand().getMessage());
      this.assertStatusCodeIsValid(
          decodedMessageQueryResponse.getResponseEnvelope().getResponseCode());
    }
  }
}
