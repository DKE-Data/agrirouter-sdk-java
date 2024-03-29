package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.commons.MessageOuterClass;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotDecodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Internal service implementation.
 */
public class DecodeMessageServiceImpl extends NonEnvironmentalService
        implements DecodeMessageService {

    @Override
    public DecodeMessageResponse decode(String encodedResponse) {
        this.logMethodBegin(encodedResponse);

        if (StringUtils.isBlank(encodedResponse)) {
            throw new IllegalArgumentException("Please provide a valid encoded response.");
        }
        try {

            this.getNativeLogger().trace("Decoding byte array.");
            var decodedBytes = Base64.getDecoder().decode(encodedResponse);
            var inputStream = new ByteArrayInputStream(decodedBytes);

            this.getNativeLogger().trace("Parse response envelope.");
            var responseEnvelope =
                    agrirouter.response.Response.ResponseEnvelope.parseDelimitedFrom(inputStream);

            this.getNativeLogger().trace("Parse response payload wrapper.");
            var responsePayloadWrapper =
                    agrirouter.response.Response.ResponsePayloadWrapper.parseDelimitedFrom(inputStream);
            var decodeMessageResponse = new DecodeMessageResponse();
            decodeMessageResponse.setResponseEnvelope(responseEnvelope);
            decodeMessageResponse.setResponsePayloadWrapper(responsePayloadWrapper);

            this.logMethodEnd(decodeMessageResponse);
            return decodeMessageResponse;
        } catch (IOException e) {
            throw new CouldNotDecodeMessageException(e);
        }
    }

    @Override
    public MessageOuterClass.Message decode(ByteString message) {
        try {
            this.logMethodBegin(message);

            this.getNativeLogger().trace("Decoding byte string.");
            var decodedMessage = MessageOuterClass.Message.parseFrom(message);

            this.logMethodEnd(decodedMessage);
            return decodedMessage;
        } catch (InvalidProtocolBufferException e) {
            throw new CouldNotDecodeMessageException(e);
        }
    }

    @Override
    public MessageOuterClass.Messages decode(Any any) {
        try {
            this.logMethodBegin(any);
            var messages =
                    any.unpack(agrirouter.commons.MessageOuterClass.Messages.class);
            this.logMethodEnd();
            return messages;
        } catch (InvalidProtocolBufferException e) {
            throw new CouldNotDecodeMessageException(e);
        }
    }
}
