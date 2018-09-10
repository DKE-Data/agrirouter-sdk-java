package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.exception.CouldNotEncodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.google.protobuf.Any;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Internal service implementation.
 */
public class EncodeMessageServiceImpl implements EncodeMessageService {


    public String encode(MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters) {
        if (null == messageHeaderParameters || null == payloadParameters) {
            throw new IllegalArgumentException("Parameters cannot be NULL");
        }
        messageHeaderParameters.validate();
        payloadParameters.validate();
        try (ByteArrayOutputStream streamedMessage = new ByteArrayOutputStream()) {
            this.header(messageHeaderParameters).writeDelimitedTo(streamedMessage);
            this.payload(payloadParameters).writeDelimitedTo(streamedMessage);
            return Base64.getEncoder().encodeToString(streamedMessage.toByteArray());
        } catch (IOException e) {
            throw new CouldNotEncodeMessageException(e);
        }

    }

    private Request.RequestEnvelope header(MessageHeaderParameters parameters) {
        agrirouter.request.Request.RequestEnvelope.Builder messageHeader = Request.RequestEnvelope.newBuilder();
        messageHeader.setApplicationMessageId(parameters.getApplicationMessageId());
        messageHeader.setApplicationMessageSeqNo(parameters.getApplicationMessageSeqNo());
        messageHeader.setTechnicalMessageType(parameters.getTechnicalMessageType().getKey());
        messageHeader.setMode(parameters.getMode());
        if (StringUtils.isNotBlank(parameters.getTeamSetContextId())) {
            messageHeader.setTeamSetContextId(parameters.getTeamSetContextId());
        }
        if (!parameters.getRecipients().isEmpty()) {
            messageHeader.addAllRecipients(parameters.getRecipients());
        }
        messageHeader.setTimestamp(new TimestampUtil().current());
        return messageHeader.build();
    }

    private Request.RequestPayloadWrapper payload(PayloadParameters parameters) {
        Request.RequestPayloadWrapper.Builder messagePayload = Request.RequestPayloadWrapper.newBuilder();
        Any.Builder builder = Any.newBuilder();
        builder.setTypeUrl(parameters.getTypeUrl());
        builder.setValue(parameters.getValue());
        messagePayload.setDetails(builder.build());
        return messagePayload.build();
    }


}
