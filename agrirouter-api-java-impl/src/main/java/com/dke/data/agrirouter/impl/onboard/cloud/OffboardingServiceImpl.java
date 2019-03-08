package com.dke.data.agrirouter.impl.onboard.cloud;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.commons.MessageOuterClass;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageRequest;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.CouldNotOffboardVirtualCommunicationUnitException;
import com.dke.data.agrirouter.api.factories.impl.CloudEndpointOffboardingMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.CloudEndpointOffboardingMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.onboard.cloud.OffboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.json.DecodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.json.EncodeMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.dke.data.agrirouter.impl.messaging.rest.json.FetchMessageServiceJSONImpl;
import com.dke.data.agrirouter.impl.messaging.rest.json.MessageSenderJSONImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OffboardingServiceImpl
    implements OffboardingService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;
  private final MessageSender messageSender;

  public OffboardingServiceImpl() {
    this(
        new EncodeMessageServiceJSONImpl(),
        new MessageSenderJSONImpl(),
        new FetchMessageServiceJSONImpl(),
        new DecodeMessageServiceJSONImpl());
  }

  public OffboardingServiceImpl(
      EncodeMessageService encodeMessageService,
      MessageSender messageSender,
      FetchMessageService fetchMessageService,
      DecodeMessageService decodeMessageService) {
    this.encodeMessageService = encodeMessageService;
    this.fetchMessageService = fetchMessageService;
    this.decodeMessageService = decodeMessageService;
    this.messageSender = messageSender;
  }

  /**
   * Offboarding a virtual CU. Will deliver no result if the action was successful, if there's any
   * error an exception will be thrown.
   *
   * @param parameters Parameters for offboarding.
   */
  @Override
  public void offboard(CloudOffboardingParameters parameters) {
    parameters.validate();
    EncodeMessageResponse encodedMessageResponse = this.encodeOffboardingMessage(parameters);
    SendMessageParameters sendMessageParameters =
        createSendMessageParameters(encodedMessageResponse, parameters.getOnboardingResponse());
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        sendMessageAndFetchResponses(sendMessageParameters, parameters.getOnboardingResponse());
    if (fetchMessageResponses.isPresent()) {
      DecodeMessageResponse decodedMessageQueryResponse =
          this.decodeMessageService.decode(
              fetchMessageResponses.get().get(0).getCommand().getMessage());
      try {
        this.assertStatusCodeIsValid(
            decodedMessageQueryResponse.getResponseEnvelope().getResponseCode());
      } catch (Exception e) {
        MessageOuterClass.Message message =
            this.decodeMessageService.decode(
                decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails().getValue());
        throw new CouldNotOffboardVirtualCommunicationUnitException(message.getMessage());
      }
    }
  }

  private Optional<List<FetchMessageResponse>> sendMessageAndFetchResponses(
      SendMessageParameters sendMessageParameters, OnboardingResponse onboardingResponse) {
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsValid(response.getNativeResponse().getStatus());
    return this.fetchMessageService.fetch(
        onboardingResponse, MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
  }

  private EncodeMessageResponse encodeOffboardingMessage(CloudOffboardingParameters parameters) {
    final String applicationMessageID = MessageIdService.generateMessageId();

    CloudEndpointOffboardingMessageParameters cloudOffboardingParameters =
        new CloudEndpointOffboardingMessageParameters();
    cloudOffboardingParameters.setEndpointIds(new ArrayList<>());
    parameters
        .getEndpointIds()
        .forEach(
            endpointId -> {
              cloudOffboardingParameters.getEndpointIds().add(endpointId);
            });

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        CloudVirtualizedAppRegistration.OffboardingRequest.getDescriptor().getFullName());

    payloadParameters.setValue(
        new CloudEndpointOffboardingMessageContentFactory().message(cloudOffboardingParameters));

    return this.encodeMessageService.encode(
        this.createMessageHeaderParameters(applicationMessageID), payloadParameters);
  }

  private MessageHeaderParameters createMessageHeaderParameters(String applicationMessageID) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);
    messageHeaderParameters.setTechnicalMessageType(
        TechnicalMessageType.DKE_CLOUD_OFFBOARD_ENDPOINTS);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
    return messageHeaderParameters;
  }

  private SendMessageParameters createSendMessageParameters(
      EncodeMessageResponse encodedMessageResponse, OnboardingResponse onboardingResponse) {
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(onboardingResponse);
    sendMessageParameters.setEncodeMessageResponse(encodedMessageResponse);
    return sendMessageParameters;
  }

  @Override
  public MessageRequest createSendMessageRequest(SendMessageParameters parameters) {
    return this.messageSender.createSendMessageRequest(parameters);
  }

  @Override
  public MessageSenderResponse sendMessage(SendMessageParameters parameters) {
    return this.messageSender.sendMessage(parameters);
  }
}
