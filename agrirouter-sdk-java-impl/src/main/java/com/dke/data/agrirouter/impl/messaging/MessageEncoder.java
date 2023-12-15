package com.dke.data.agrirouter.impl.messaging;

import agrirouter.cloud.registration.CloudVirtualizedAppRegistration;
import agrirouter.commons.MessageOuterClass;
import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import agrirouter.request.payload.endpoint.Capabilities;
import agrirouter.request.payload.endpoint.SubscriptionOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.HasLogger;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.dke.data.agrirouter.impl.common.MessageIdService;

import java.util.Objects;

public interface MessageEncoder extends HasLogger {

    /**
     * Encode a message to delete messages.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(DeleteMessageParameters parameters) {
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();
        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_FEED_DELETE);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var messageContent = FeedRequests.MessageDelete.newBuilder();
        if (parameters.getMessageIds() != null) {
            messageContent.addAllMessageIds(parameters.getMessageIds());
        }
        if (parameters.getSenderIds() != null) {
            messageContent.addAllSenders(parameters.getSenderIds());
        }
        if (null != parameters.getSentFromInSeconds() || null != parameters.getSentToInSeconds()) {
            var validityPeriod = FeedRequests.ValidityPeriod.newBuilder();
            if (null != parameters.getSentFromInSeconds()) {
                validityPeriod.setSentFrom(new TimestampUtil().seconds(parameters.getSentFromInSeconds()));
            }
            if (null != parameters.getSentToInSeconds()) {
                validityPeriod.setSentTo(new TimestampUtil().seconds(parameters.getSentToInSeconds()));
            }
            messageContent.setValidityPeriod(validityPeriod);
        }

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_FEED_DELETE.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode a message to list endpoints.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(ListEndpointsParameters parameters) {
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();
        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        if (parameters.getUnfilteredList()) {
            messageHeaderParameters.setTechnicalMessageType(
                    SystemMessageType.DKE_LIST_ENDPOINTS_UNFILTERED);
        } else {
            messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_LIST_ENDPOINTS);
        }
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var messageContent = Endpoints.ListEndpointsQuery.newBuilder();
        messageContent.setDirection(Objects.requireNonNull(parameters.getDirection()));
        messageContent.setTechnicalMessageType(
                Objects.requireNonNull(parameters.getTechnicalMessageType()).getKey());

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_LIST_ENDPOINTS.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);

        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode a message to send a message confirmation.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(MessageConfirmationParameters parameters) {
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_FEED_CONFIRM);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var messageContent = FeedRequests.MessageConfirm.newBuilder();
        messageContent.addAllMessageIds(Objects.requireNonNull(parameters.getMessageIds()));

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_FEED_CONFIRM.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode a message to set capabilities.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(SetCapabilitiesParameters parameters) {
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_CAPABILITIES);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var builder =
                Capabilities.CapabilitySpecification.newBuilder();
        builder.setAppCertificationId(Objects.requireNonNull(parameters.getApplicationId()));
        builder.setAppCertificationVersionId(
                Objects.requireNonNull(parameters.getCertificationVersionId()));
        builder.setEnablePushNotifications(parameters.getEnablePushNotifications());

        parameters.getCapabilitiesParameters();
        Objects.requireNonNull(parameters.getCapabilitiesParameters())
                .forEach(
                        p -> {
                            var capabilityBuilder =
                                    Capabilities.CapabilitySpecification.Capability.newBuilder();
                            capabilityBuilder.setTechnicalMessageType(
                                    Objects.requireNonNull(p.getTechnicalMessageType()).getKey());
                            capabilityBuilder.setDirection(Objects.requireNonNull(p.getDirection()));
                            var capability =
                                    capabilityBuilder.build();
                            builder.addCapabilities(capability);
                        });

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_CAPABILITIES.getTypeUrl());
        payloadParameters.setValue(builder.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    static void setSequenceNumber(
            MessageHeaderParameters messageHeaderParameters,
            long sequenceNumber,
            OnboardingResponse onboardingResponse) {
        if (sequenceNumber == 0 && onboardingResponse == null) {
            throw new IllegalArgumentException(
                    "Either sequence number or onboarding response must be set.");
        }

        messageHeaderParameters.setApplicationMessageSeqNo(
                sequenceNumber != 0
                        ? sequenceNumber
                        : SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
    }

    /**
     * Encode a message to set a subscription.
     *
     * @param parameters -
     * @return -
     */
    @Deprecated
    default EncodedMessage encodeMessage(SetSubscriptionParameters parameters) {
        return encode(parameters);
    }

    /**
     * Encode a message to set a subscription.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(SetSubscriptionParameters parameters) {
        assert parameters.getOnboardingResponse() != null;
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_SUBSCRIPTION);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var messageContent =
                SubscriptionOuterClass.Subscription.newBuilder();
        parameters
                .getSubscriptions()
                .forEach(
                        parameter -> {
                            var
                                    technicalMessageType =
                                    SubscriptionOuterClass.Subscription.MessageTypeSubscriptionItem.newBuilder();
                            technicalMessageType.setTechnicalMessageType(
                                    Objects.requireNonNull(parameter.getTechnicalMessageType()).getKey());
                            technicalMessageType.addAllDdis(parameter.getDdis());
                            technicalMessageType.setPosition(parameter.getPosition());
                            messageContent.addTechnicalMessageTypes(technicalMessageType);
                        });

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_SUBSCRIPTION.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode a message to query messages.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(
            TechnicalMessageType technicalMessageType, MessageQueryParameters parameters) {
        this.logMethodBegin(parameters);
        this.getNativeLogger().trace("Build message header parameters.");
        var messageHeaderParameters = new MessageHeaderParameters();

        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setTechnicalMessageType(technicalMessageType);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        this.getNativeLogger().trace("Build message query parameters.");
        var messageContent = FeedRequests.MessageQuery.newBuilder();
        if (parameters.getMessageIds() != null) {
            messageContent.addAllMessageIds(parameters.getMessageIds());
        }
        if (parameters.getSenderIds() != null) {
            messageContent.addAllSenders(parameters.getSenderIds());
        }
        if (null != parameters.getSentFromInSeconds() || null != parameters.getSentToInSeconds()) {
            var validityPeriod = FeedRequests.ValidityPeriod.newBuilder();
            if (null != parameters.getSentFromInSeconds()) {
                validityPeriod.setSentFrom(new TimestampUtil().seconds(parameters.getSentFromInSeconds()));
            }
            if (null != parameters.getSentToInSeconds()) {
                validityPeriod.setSentTo(new TimestampUtil().seconds(parameters.getSentToInSeconds()));
            }
            messageContent.setValidityPeriod(validityPeriod);
        }

        this.getNativeLogger().trace("Build message payload parameters.");
        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(technicalMessageType.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        this.getNativeLogger().trace("Encode message.");
        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);

        this.logMethodEnd(encodedMessage);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode message for cloud onboarding of virtual CUs.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(CloudOnboardingParameters parameters) {
        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        final var teamsetContextId =
                parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();

        var messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setApplicationMessageId(applicationMessageID);
        messageHeaderParameters.setTeamSetContextId(teamsetContextId);

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_CLOUD_ONBOARD_ENDPOINTS);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        var messageContent =
                CloudVirtualizedAppRegistration.OnboardingRequest.newBuilder();
        Objects.requireNonNull(parameters.getEndpointDetails())
                .forEach(
                        p -> {
                            var
                                    builder =
                                    CloudVirtualizedAppRegistration.OnboardingRequest.EndpointRegistrationDetails
                                            .newBuilder();
                            builder.setId(Objects.requireNonNull(p.getEndpointId()));
                            builder.setName(Objects.requireNonNull(p.getEndpointName()));
                            messageContent.addOnboardingRequests(builder.build());
                        });

        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_CLOUD_ONBOARD_ENDPOINTS.getTypeUrl());
        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Encode cloud offboarding message.
     *
     * @param parameters -
     * @return -
     */
    default EncodedMessage encode(CloudOffboardingParameters parameters) {
        final var applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();

        var messageContent =
                CloudVirtualizedAppRegistration.OffboardingRequest.newBuilder();
        messageContent.addAllEndpoints(Objects.requireNonNull(parameters.getEndpointIds()));

        var messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setApplicationMessageId(applicationMessageID);
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_CLOUD_OFFBOARD_ENDPOINTS);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
        messageHeaderParameters.setMetadata(MessageOuterClass.Metadata.newBuilder().build());

        setSequenceNumber(
                messageHeaderParameters,
                parameters.getSequenceNumber(),
                parameters.getOnboardingResponse());
        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(SystemMessageType.DKE_CLOUD_OFFBOARD_ENDPOINTS.getTypeUrl());

        payloadParameters.setValue(messageContent.build().toByteString());

        var encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    /**
     * Get the service to encode messages.
     *
     * @return -
     */
    EncodeMessageService getEncodeMessageService();
}
