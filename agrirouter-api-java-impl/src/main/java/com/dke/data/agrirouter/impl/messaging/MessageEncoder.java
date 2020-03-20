package com.dke.data.agrirouter.impl.messaging;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import agrirouter.request.payload.account.Endpoints;
import agrirouter.request.payload.endpoint.Capabilities;
import agrirouter.request.payload.endpoint.SubscriptionOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.MessageConfirmationMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.MessageQueryMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.SubscriptionMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageConfirmationMessageParameters;
import com.dke.data.agrirouter.api.factories.impl.parameters.MessageQueryMessageParameters;
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters;
import com.dke.data.agrirouter.api.service.LoggingEnabledService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Objects;

public interface MessageEncoder extends LoggingEnabledService {

  /**
   * Encode a message to delete messages.
   *
   * @param parameters -
   * @return -
   */
  default EncodedMessage encode(DeleteMessageParameters parameters) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();
    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());

    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_DELETE);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    FeedRequests.MessageDelete.Builder messageContent = FeedRequests.MessageDelete.newBuilder();
    messageContent.addAllMessageIds(parameters.getMessageIds());
    messageContent.addAllSenders(parameters.getSenderIds());
    if (null != parameters.getSentFromInSeconds() || null != parameters.getSentToInSeconds()) {
      FeedRequests.ValidityPeriod.Builder validityPeriod = FeedRequests.ValidityPeriod.newBuilder();
      if (null != parameters.getSentFromInSeconds()) {
        validityPeriod.setSentFrom(new TimestampUtil().seconds(parameters.getSentFromInSeconds()));
      }
      if (null != parameters.getSentToInSeconds()) {
        validityPeriod.setSentTo(new TimestampUtil().seconds(parameters.getSentToInSeconds()));
      }
      messageContent.setValidityPeriod(validityPeriod);
    }

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(FeedRequests.MessageDelete.getDescriptor().getFullName());
    payloadParameters.setValue(messageContent.build().toByteString());

    String encodedMessage =
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

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();
    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());

    if (parameters.getUnfilteredList()) {
      messageHeaderParameters.setTechnicalMessageType(
          TechnicalMessageType.DKE_LIST_ENDPOINTS_UNFILTERED);
    } else {
      messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_LIST_ENDPOINTS);
    }
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    Endpoints.ListEndpointsQuery.Builder messageContent = Endpoints.ListEndpointsQuery.newBuilder();
    messageContent.setDirection(parameters.direction);
    messageContent.setTechnicalMessageType(parameters.technicalMessageType.getKey());

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(Endpoints.ListEndpointsQuery.getDescriptor().getFullName());
    payloadParameters.setValue(messageContent.build().toByteString());

    String encodedMessage =
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
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());
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

    String encodedMessage =
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
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_CAPABILITIES);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    Capabilities.CapabilitySpecification.Builder messageContent =
        Capabilities.CapabilitySpecification.newBuilder();
    messageContent.setAppCertificationId(parameters.getApplicationId());
    messageContent.setAppCertificationVersionId(parameters.getCertificationVersionId());
    messageContent.setEnablePushNotifications(parameters.getEnablePushNotifications());

    parameters.getCapabilitiesParameters();
    parameters
        .getCapabilitiesParameters()
        .forEach(
            p -> {
              Capabilities.CapabilitySpecification.Capability.Builder capabilityBuilder =
                  Capabilities.CapabilitySpecification.Capability.newBuilder();
              capabilityBuilder.setTechnicalMessageType(p.technicalMessageType.getKey());
              capabilityBuilder.setDirection(p.direction);
              Capabilities.CapabilitySpecification.Capability capability =
                  capabilityBuilder.build();
              messageContent.getCapabilitiesList().add(capability);
            });

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        Capabilities.CapabilitySpecification.getDescriptor().getFullName());
    payloadParameters.setValue(messageContent.build().toByteString());

    String encodedMessage =
        this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
    return new EncodedMessage(applicationMessageID, encodedMessage);
  }

  /**
   * Encode a message to set a subscription.
   *
   * @param parameters -
   * @return -
   */
  default EncodedMessage encodeMessage(SetSubscriptionParameters parameters) {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_SUBSCRIPTION);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    SubscriptionMessageParameters subscriptionList = new SubscriptionMessageParameters();
    subscriptionList.setSubscriptions(new ArrayList<>());

    for (SetSubscriptionParameters.Subscription entry : parameters.getSubscriptions()) {
      SubscriptionMessageParameters.SubscriptionMessageEntry messageTypeSubscriptionItem =
          new SubscriptionMessageParameters.SubscriptionMessageEntry();
      messageTypeSubscriptionItem.setTechnicalMessageType(entry.getTechnicalMessageType());
      messageTypeSubscriptionItem.setDdis(entry.getDdis());
      messageTypeSubscriptionItem.setPosition(entry.getPosition());
      subscriptionList.getSubscriptions().add(messageTypeSubscriptionItem);
    }

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(SubscriptionOuterClass.Subscription.getDescriptor().getFullName());

    ByteString messageValue = new SubscriptionMessageContentFactory().message(subscriptionList);
    payloadParameters.setValue(messageValue);

    String encodedMessage =
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
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

    final String applicationMessageID =
        parameters.getApplicationMessageId() == null
            ? MessageIdService.generateMessageId()
            : parameters.getApplicationMessageId();

    messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

    final String teamsetContextId =
        parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
    messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));
    messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());
    messageHeaderParameters.setTechnicalMessageType(technicalMessageType);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

    this.getNativeLogger().trace("Build message query parameters.");
    MessageQueryMessageParameters messageQueryMessageParameters =
        new MessageQueryMessageParameters();
    messageQueryMessageParameters.setMessageIds(Objects.requireNonNull(parameters.getMessageIds()));
    messageQueryMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
    messageQueryMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
    messageQueryMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());

    this.getNativeLogger().trace("Build message payload parameters.");
    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(FeedRequests.MessageQuery.getDescriptor().getFullName());
    payloadParameters.setValue(
        new MessageQueryMessageContentFactory().message(messageQueryMessageParameters));

    this.getNativeLogger().trace("Encode message.");
    String encodedMessage =
        this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);

    this.logMethodEnd(encodedMessage);
    return new EncodedMessage(applicationMessageID, encodedMessage);
  }

  /**
   * Get the service to encode messages.
   *
   * @return -
   */
  EncodeMessageService getEncodeMessageService();
}
