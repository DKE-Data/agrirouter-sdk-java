package com.dke.data.agrirouter.impl.messaging.rest;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.feed.response.FeedResponse;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.MessageConfirmationService;
import com.dke.data.agrirouter.api.service.messaging.MessageQueryService;
import com.dke.data.agrirouter.api.service.messaging.convenience.MessageConfirmationFunctionsService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.*;

public class MessageConfirmationServiceImpl extends EnvironmentalService
    implements MessageConfirmationService,
        MessageConfirmationFunctionsService,
        MessageSender,
        MessageEncoder,
        ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final MessageQueryService messageQueryService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;

  public MessageConfirmationServiceImpl(Environment environment) {
    super(environment);
    this.encodeMessageService = new EncodeMessageServiceImpl();
    this.messageQueryService = new MessageQueryServiceImpl(environment);
    this.fetchMessageService = new FetchMessageServiceImpl();
    this.decodeMessageService = new DecodeMessageServiceImpl();
  }

  @Override
  public String send(MessageConfirmationParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessage = this.encode(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessage.getEncodedMessage()));
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodedMessage.getApplicationMessageID();
  }

  @Override
  public void confirmAllPendingMessagesWithValidation(
      MessageConfirmationForAllPendingMessagesParameters parameters) {
    this.confirmAllPendingMessages(parameters, true);
  }

  @Override
  public void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters) {
    this.confirmAllPendingMessages(parameters, false);
  }

  private void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters, boolean enableValidation) {
    MessageQueryParameters messageQueryParameters = new MessageQueryParameters();
    messageQueryParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    messageQueryParameters.setMessageIds(Collections.emptyList());
    messageQueryParameters.setSenderIds(Collections.emptyList());
    messageQueryParameters.setSentFromInSeconds(
        UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
    messageQueryParameters.setSentToInSeconds(UtcTimeService.now().toEpochSecond());

    this.messageQueryService.send(messageQueryParameters);

    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        this.fetchMessageService.fetch(
            parameters.getOnboardingResponse(), MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
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
        this.send(messageConfirmationParameters);
        if (enableValidation) {
          this.validateResponse(parameters);
        }
      }
    }
  }

  private void validateResponse(MessageConfirmationForAllPendingMessagesParameters parameters) {
    Optional<List<FetchMessageResponse>> fetchMessageResponses;
    DecodeMessageResponse decodedMessageQueryResponse;
    fetchMessageResponses =
        this.fetchMessageService.fetch(
            parameters.getOnboardingResponse(), MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
    if (fetchMessageResponses.isPresent()) {
      decodedMessageQueryResponse =
          this.decodeMessageService.decode(
              fetchMessageResponses.get().get(0).getCommand().getMessage());
      this.assertStatusCodeIsValid(
          decodedMessageQueryResponse.getResponseEnvelope().getResponseCode());
    }
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
