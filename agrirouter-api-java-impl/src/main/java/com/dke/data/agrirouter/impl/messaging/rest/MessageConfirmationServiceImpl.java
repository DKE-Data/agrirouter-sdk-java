package com.dke.data.agrirouter.impl.messaging.rest;

import static com.dke.data.agrirouter.api.service.messaging.FetchMessageService.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.api.service.messaging.FetchMessageService.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.feed.request.FeedRequests;
import agrirouter.feed.response.FeedResponse;
import agrirouter.request.Request;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.env.Environment;
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
import com.dke.data.agrirouter.impl.messaging.encoding.json.DecodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.FetchMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageQueryServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MessageConfirmationServiceImpl<SenderType> extends EnvironmentalService
    implements MessageSender<SenderType>, MessageConfirmationService, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final MessageSender messageSender;
  private final MessageQueryService messageQueryService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;

  /**
   * @param -
   * @deprecated As the interface offers JSON and Protobuf, the used format has to be defined Use
   *     DeleteMessageServiceJSONImpl or DeleteMessageServiceProtobufImpl instead
   */
  @Deprecated
  public MessageConfirmationServiceImpl(Environment environment) {
    this(
        environment,
        new EncodeMessageServiceJSONImpl(),
        new DecodeMessageServiceJSONImpl(),
        new MessageSenderJSONImpl(),
        new MessageQueryServiceJSONImpl(environment),
        new FetchMessageServiceJSONImpl());
  }

  public MessageConfirmationServiceImpl(
      Environment environment,
      EncodeMessageService encodeMessageService,
      DecodeMessageService decodeMessageService,
      MessageSender messageSender,
      MessageQueryService messageQueryService,
      FetchMessageService fetchMessageService) {
    super(environment);
    this.encodeMessageService = encodeMessageService;
    this.messageQueryService = messageQueryService;
    this.fetchMessageService = fetchMessageService;
    this.decodeMessageService = decodeMessageService;
    this.messageSender = messageSender;
  }

  @Override
  public String send(MessageConfirmationParameters parameters) {
    parameters.validate();

    EncodeMessageResponse encodedMessageResponse = encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodeMessageResponse(encodedMessageResponse);

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return encodedMessageResponse.getApplicationMessageID();
  }

  private EncodeMessageResponse encodeMessage(MessageConfirmationParameters parameters) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID = MessageIdService.generateMessageId();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);

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
          this.validateReponse(parameters);
        }
      }
    }
  }

  private void validateReponse(MessageConfirmationForAllPendingMessagesParameters parameters) {
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
  public SenderType createSendMessageRequest(SendMessageParameters parameters) {
    return (SenderType) this.messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
