package com.dke.data.agrirouter.convenience.decode;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.commons.MessageOuterClass;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.exception.CouldNotOnboardVirtualCommunicationUnitException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience implementation to decode multiple onboarding responses from the fetched mesage.
 */
public class DecodeCloudOnboardingResponsesService
        implements ResponseValidator,
        MessageDecoder<CloudVirtualizedAppRegistration.OnboardingResponse> {

    private final DecodeMessageService decodeMessageService;

    public DecodeCloudOnboardingResponsesService() {
        this.decodeMessageService = new DecodeMessageServiceImpl();
    }

    /**
     * Decode onbarding response for cloud onboarding given the message responses.
     *
     * @param fetchMessageResponses -
     * @param onboardingResponse    -
     * @return -
     */
    public List<OnboardingResponse> decode(
            List<FetchMessageResponse> fetchMessageResponses, OnboardingResponse onboardingResponse) {
        List<OnboardingResponse> responses = new ArrayList<>();
        DecodeMessageResponse decodedMessageQueryResponse =
                this.decodeMessageService.decode(fetchMessageResponses.get(0).getCommand().getMessage());
        try {
            this.assertStatusCodeIsValid(
                    decodedMessageQueryResponse.getResponseEnvelope().getResponseCode());
        } catch (Exception e) {
            MessageOuterClass.Messages message =
                    this.decodeMessageService.decode(
                            decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails());
            throw new CouldNotOnboardVirtualCommunicationUnitException(
                    message.getMessages(0).getMessage());
        }
        if (decodedMessageQueryResponse.getResponseEnvelope().getType()
                == Response.ResponseEnvelope.ResponseBodyType.CLOUD_REGISTRATIONS
                && this.assertStatusCodeIsCreated(
                decodedMessageQueryResponse.getResponseEnvelope().getResponseCode())) {
            CloudVirtualizedAppRegistration.OnboardingResponse cloudOnboardingResponse =
                    this.decode(
                            decodedMessageQueryResponse.getResponsePayloadWrapper().getDetails().getValue());
            cloudOnboardingResponse
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
                                        onboardingResponse.getAuthentication());
                                internalOnboardingResponse.setConnectionCriteria(
                                        onboardingResponse.getConnectionCriteria());
                                responses.add(internalOnboardingResponse);
                            });
        }
        return responses;
    }

    @Override
    public CloudVirtualizedAppRegistration.OnboardingResponse unsafeDecode(ByteString message)
            throws InvalidProtocolBufferException {
        return CloudVirtualizedAppRegistration.OnboardingResponse.parseFrom(message);
    }
}
