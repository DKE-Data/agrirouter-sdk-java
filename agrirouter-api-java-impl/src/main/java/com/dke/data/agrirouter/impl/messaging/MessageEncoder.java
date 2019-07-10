package com.dke.data.agrirouter.impl.messaging;

import agrirouter.feed.request.FeedRequests;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.encoding.EncodedMessage;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.DeleteMessageMessageContentFactory;
import com.dke.data.agrirouter.api.factories.impl.parameters.DeleteMessageMessageParameters;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;

import java.util.Objects;

public interface MessageEncoder {

    default EncodedMessage encode(DeleteMessageParameters parameters) {
        MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();

        final String applicationMessageID =
                parameters.getApplicationMessageId() == null
                        ? MessageIdService.generateMessageId()
                        : parameters.getApplicationMessageId();
        messageHeaderParameters.setApplicationMessageId(Objects.requireNonNull(applicationMessageID));

        final String teamsetContextId = parameters.getTeamsetContextId() == null ? "" : parameters.getTeamsetContextId();
        messageHeaderParameters.setTeamSetContextId(Objects.requireNonNull(teamsetContextId));

        messageHeaderParameters.setApplicationMessageSeqNo(parameters.getSequenceNumber());

        messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_FEED_DELETE);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);

        DeleteMessageMessageParameters deleteMessageMessageParameters =
                new DeleteMessageMessageParameters();
        deleteMessageMessageParameters.setMessageIds(
                Objects.requireNonNull(parameters.getMessageIds()));
        deleteMessageMessageParameters.setSenderIds(Objects.requireNonNull(parameters.getSenderIds()));
        deleteMessageMessageParameters.setSentFromInSeconds(parameters.getSentFromInSeconds());
        deleteMessageMessageParameters.setSentToInSeconds(parameters.getSentToInSeconds());

        PayloadParameters payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(FeedRequests.MessageDelete.getDescriptor().getFullName());
        payloadParameters.setValue(
                new DeleteMessageMessageContentFactory().message(deleteMessageMessageParameters));

        String encodedMessage =
                this.getEncodeMessageService().encode(messageHeaderParameters, payloadParameters);
        return new EncodedMessage(applicationMessageID, encodedMessage);
    }

    EncodeMessageService getEncodeMessageService();
}
