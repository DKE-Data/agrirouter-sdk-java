package com.dke.data.agrirouter.impl.messaging.rest;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.feed.request.FeedRequests;
import agrirouter.feed.response.FeedResponse;
import agrirouter.request.Request;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import com.dke.data.agrirouter.api.factories.impl.MessageConfirmationMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.MessageConfirmationService;
import com.dke.data.agrirouter.api.service.messaging.MessageQueryService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.impl.EnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.*;
import org.apache.http.HttpStatus;

public class MessageConfirmationServiceImpl extends EnvironmentalService
    implements MessageConfirmationService, MessageSender, ResponseValidator {

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
  public void send(MessageConfirmationParameters parameters) {
    parameters.validate();

    String encodedMessage = encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(Collections.singletonList(encodedMessage));

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK, false);
  }

  private String encodeMessage(MessageConfirmationParameters parameters) {
    String applicationMessageId = MessageIdService.generateMessageId();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageId);
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_CONFIRM);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    MessageConfirmationMessageParameters messageConfirmationMessageParameters =
        new MessageConfirmationMessageParameters();
    messageConfirmationMessageParameters.setMessageIds(parameters.getMessageIds());

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(FeedRequests.MessageConfirm.getDescriptor().getFullName());
    payloadParameters.setValue(
        new MessageConfirmationMessageContentFactory()
            .message(messageConfirmationMessageParameters));

    return this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
  }

  @Override
  public void confirmAllPendingMessages(
      MessageConfirmationForAllPendingMessagesParameters parameters) {
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
          && decodedMessageQueryResponse.getResponseEnvelope().getResponseCode()
              == HttpStatus.SC_OK) {
        FeedResponse.MessageQueryResponse messageQueryResponse =
            this.messageQueryService.decode(
                decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails().getValue());
        List<String> messageIds = new ArrayList<>();
        messageQueryResponse
            .getMessagesList()
            .forEach(feedMessage -> messageIds.add(feedMessage.getHeader().getMessageId()));
        MessageConfirmationParameters messageConfirmationParameters =
            new MessageConfirmationParameters();
        messageConfirmationParameters.setApplicationId(parameters.getApplicationId());
        messageConfirmationParameters.setCertificationVersionId(
            parameters.getCertificationVersionId());
        messageConfirmationParameters.setOnboardingResponse(parameters.getOnboardingResponse());
        messageConfirmationParameters.setMessageIds(messageIds);
        this.send(messageConfirmationParameters);
        fetchMessageResponses =
            this.fetchMessageService.fetch(
                parameters.getOnboardingResponse(), MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
        if (fetchMessageResponses.isPresent()) {
          decodedMessageQueryResponse =
              this.decodeMessageService.decode(
                  fetchMessageResponses.get().get(0).getCommand().getMessage());
          if (decodedMessageQueryResponse.getResponseEnvelope().getResponseCode()
              != HttpStatus.SC_CREATED) {
            throw new UnexpectedHttpStatusException(
                decodedMessageQueryResponse.getResponseEnvelope().getResponseCode(),
                HttpStatus.SC_CREATED);
          }
        }
      }
    }
  }
}
