package com.dke.data.agrirouter.impl.onboard.cloud;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.commons.MessageOuterClass;
import agrirouter.request.Request;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.CouldNotOnboardVirtualCommunicationUnitException;
import com.dke.data.agrirouter.api.factories.impl.CloudEndpointOffboardingMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.CloudEndpointOnboardingMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.CloudEndpointOffboardingMessageParameters;
import com.dke.data.agrirouter.api.factories.impl.parameters.CloudEndpointOnboardingMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.onboard.cloud.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.MessageSender;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OnboardingServiceImpl implements OnboardingService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;

  public OnboardingServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
    this.fetchMessageService = new FetchMessageServiceImpl();
    this.decodeMessageService = new DecodeMessageServiceImpl();
  }

  /**
   * Onboarding a virtual CU for an existing cloud application (incl. several checks).
   *
   * @param parameters Parameters for the onboarding.
   * @return -
   */
  @Override
  public List<OnboardingResponse> onboard(CloudOnboardingParameters parameters) {
    parameters.validate();
    EncodedMessage encodedMessageResponse = this.encodeOnboardingMessage(parameters);
    SendMessageParameters sendMessageParameters =
        createSendMessageParameters(encodedMessageResponse, parameters.getOnboardingResponse());
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        sendMessageAndFetchResponses(sendMessageParameters, parameters.getOnboardingResponse());

    List<OnboardingResponse> responses = new ArrayList<>();
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
        throw new CouldNotOnboardVirtualCommunicationUnitException(message.getMessage());
      }
      if (decodedMessageQueryResponse.getResponseEnvelope().getType()
              == Response.ResponseEnvelope.ResponseBodyType.CLOUD_REGISTRATIONS
          && this.assertStatusCodeIsCreated(
              decodedMessageQueryResponse.getResponseEnvelope().getResponseCode())) {
        CloudVirtualizedAppRegistration.OnboardingResponse onboardingResponse =
            this.decode(
                decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails().getValue());
        onboardingResponse
            .getOnboardedEndpointsList()
            .forEach(
                endpointRegistrationDetails -> {
                  OnboardingResponse internalOnboardingResponse = new OnboardingResponse();
                  internalOnboardingResponse.setSensorAlternateId(
                      endpointRegistrationDetails.getSensorAlternateId());
                  internalOnboardingResponse.setCapabilityAlternateId(
                      endpointRegistrationDetails.getCapabilityAlternateId());
                  internalOnboardingResponse.setDeviceAlternateId(
                      endpointRegistrationDetails.getDeviceAlternateId());
                  internalOnboardingResponse.setAuthentication(
                      parameters.getOnboardingResponse().getAuthentication());
                  internalOnboardingResponse.setConnectionCriteria(
                      parameters.getOnboardingResponse().getConnectionCriteria());
                  responses.add(internalOnboardingResponse);
                });
      }
    }
    return responses;
  }

  private Optional<List<FetchMessageResponse>> sendMessageAndFetchResponses(
      SendMessageParameters sendMessageParameters, OnboardingResponse onboardingResponse) {
    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsValid(response.getNativeResponse().getStatus());
    return this.fetchMessageService.fetch(
        onboardingResponse, MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
  }

  private EncodedMessage encodeOnboardingMessage(CloudOnboardingParameters parameters) {
    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    MessageHeaderParameters messageHeaderParameters =
        this.createMessageHeaderParameters(
            parameters, TechnicalMessageType.DKE_CLOUD_ONBOARD_ENDPOINTS);

    List<CloudEndpointOnboardingMessageParameters> onboardCloudEndpointMessageParameters =
        new ArrayList<>();
    parameters
        .getEndpointDetails()
        .forEach(
            endpointDetailsParameters -> {
              CloudEndpointOnboardingMessageParameters onboardCloudEndpointMessageParameter =
                  new CloudEndpointOnboardingMessageParameters();
              onboardCloudEndpointMessageParameter.setEndpointId(
                  endpointDetailsParameters.getEndpointId());
              onboardCloudEndpointMessageParameter.setEndpointName(
                  endpointDetailsParameters.getEndpointName());
              onboardCloudEndpointMessageParameters.add(onboardCloudEndpointMessageParameter);
            });

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        CloudVirtualizedAppRegistration.OnboardingRequest.getDescriptor().getFullName());
    payloadParameters.setValue(
        new CloudEndpointOnboardingMessageContentFactory()
            .message(
                onboardCloudEndpointMessageParameters.toArray(
                    new CloudEndpointOnboardingMessageParameters
                        [onboardCloudEndpointMessageParameters.size()])));

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    return new EncodedMessage(applicationMessageID, encodedMessage);
  }

  private EncodedMessage encodeOffboardingMessage(CloudOffboardingParameters parameters) {
    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    MessageHeaderParameters messageHeaderParameters =
        this.createMessageHeaderParameters(
            parameters, TechnicalMessageType.DKE_CLOUD_OFFBOARD_ENDPOINTS);

    CloudEndpointOffboardingMessageParameters cloudOffboardingParameters =
        new CloudEndpointOffboardingMessageParameters();
    cloudOffboardingParameters.setEndpointIds(new ArrayList<>());
    parameters
        .getEndpointIds()
        .forEach(endpointId -> cloudOffboardingParameters.getEndpointIds().add(endpointId));

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        CloudVirtualizedAppRegistration.OffboardingRequest.getDescriptor().getFullName());

    payloadParameters.setValue(
        new CloudEndpointOffboardingMessageContentFactory().message(cloudOffboardingParameters));

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    return new EncodedMessage(applicationMessageID, encodedMessage);
  }

  private MessageHeaderParameters createMessageHeaderParameters(
      AbstractParameterBase parameters, TechnicalMessageType technicalMessageType) {
    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();

    int sequenceNumber = parameters.getSequenceNumber();

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);
    messageHeaderParameters.setTeamSetContextId(teamsetContextId);
    messageHeaderParameters.setApplicationMessageSeqNo(sequenceNumber);
    messageHeaderParameters.setTechnicalMessageType(technicalMessageType);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
    return messageHeaderParameters;
  }

  private SendMessageParameters createSendMessageParameters(
      EncodedMessage encodedMessageResponse, OnboardingResponse onboardingResponse) {
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(onboardingResponse);
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessageResponse.getEncodedMessage()));
    return sendMessageParameters;
  }

  @Override
  public CloudVirtualizedAppRegistration.OnboardingResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return CloudVirtualizedAppRegistration.OnboardingResponse.parseFrom(message);
  }
}
