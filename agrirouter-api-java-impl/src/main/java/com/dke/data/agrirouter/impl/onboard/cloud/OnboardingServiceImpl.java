package com.dke.data.agrirouter.impl.onboard.cloud;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.commons.MessageOuterClass;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.exception.CouldNotOnboardVirtualCommunicationUnitException;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.onboard.cloud.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
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

public class OnboardingServiceImpl
    implements OnboardingService, MessageSender, ResponseValidator, MessageEncoder {

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
    EncodedMessage encodedMessageResponse = this.encode(parameters);
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

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
