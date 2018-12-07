package com.dke.data.agrirouter.impl.onboard.cloud;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.request.Request;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.OnboardCloudEndpointMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.OnboardCloudEndpointMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.FetchMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.onboard.cloud.OnboardingService;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
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
import org.apache.http.HttpStatus;

public class OnboardingServiceImpl implements OnboardingService, MessageSender, ResponseValidator {

  private final EncodeMessageService encodeMessageService;
  private final FetchMessageService fetchMessageService;
  private final DecodeMessageService decodeMessageService;

  public OnboardingServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
    this.fetchMessageService = new FetchMessageServiceImpl();
    this.decodeMessageService = new DecodeMessageServiceImpl();
  }

  public List<OnboardingResponse> onboard(CloudOnboardingParameters parameters) {
    parameters.validate();

    EncodeMessageResponse encodedMessageResponse = this.encodeMessage(parameters);
    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setEncodedMessages(
        Collections.singletonList(encodedMessageResponse.getEncodedMessage()));

    MessageSender.MessageSenderResponse response = this.sendMessage(sendMessageParameters);

    this.assertResponseStatusIsValid(response.getNativeResponse(), HttpStatus.SC_OK);

    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        this.fetchMessageService.fetch(
            parameters.getOnboardingResponse(), MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL);
    List<OnboardingResponse> responses = new ArrayList<>();
    if (fetchMessageResponses.isPresent()) {
      DecodeMessageResponse decodedMessageQueryResponse =
          this.decodeMessageService.decode(
              fetchMessageResponses.get().get(0).getCommand().getMessage());
      if (decodedMessageQueryResponse.getResponseEnvelope().getType()
              == Response.ResponseEnvelope.ResponseBodyType.CLOUD_REGISTRATIONS
          && decodedMessageQueryResponse.getResponseEnvelope().getResponseCode()
              == HttpStatus.SC_OK) {
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

  private EncodeMessageResponse encodeMessage(CloudOnboardingParameters parameters) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    final String applicationMessageID = MessageIdService.generateMessageId();
    messageHeaderParameters.setApplicationMessageId(applicationMessageID);

    messageHeaderParameters.setTechnicalMessageType(
        TechnicalMessageType.DKE_CLOUD_ONBOARD_ENDPOINTS);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    List<OnboardCloudEndpointMessageParameters> onboardCloudEndpointMessageParameters =
        new ArrayList<>();
    parameters
        .getEndpointDetails()
        .forEach(
            endpointDetailsParameters -> {
              OnboardCloudEndpointMessageParameters onboardCloudEndpointMessageParameter =
                  new OnboardCloudEndpointMessageParameters();
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
        new OnboardCloudEndpointMessageContentFactory()
            .message(
                onboardCloudEndpointMessageParameters.toArray(
                    new OnboardCloudEndpointMessageParameters[0])));

    String encodedMessage =
        this.encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    return new EncodeMessageResponse(applicationMessageID, encodedMessage);
  }

  @Override
  public CloudVirtualizedAppRegistration.OnboardingResponse unsafeDecode(ByteString message)
      throws InvalidProtocolBufferException {
    return CloudVirtualizedAppRegistration.OnboardingResponse.parseFrom(message);
  }
}
