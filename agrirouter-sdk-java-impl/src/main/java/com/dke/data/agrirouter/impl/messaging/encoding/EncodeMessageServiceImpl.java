package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.commons.Chunk;
import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.exception.CouldNotEncodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageParameterTuple;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.dke.data.agrirouter.impl.ChunkContextIdService;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.SequenceNumberService;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.dke.data.agrirouter.api.service.parameters.PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT;

/**
 * Internal service implementation.
 */
@SuppressWarnings("ConstantConditions")
public class EncodeMessageServiceImpl extends NonEnvironmentalService
        implements EncodeMessageService {

    /**
     * Encode a message. In this case chunking will be done by the application and not by the SDK.
     *
     * @param messageHeaderParameters -
     * @param payloadParameters       -
     * @return Single message, encoded by the SDK.
     */
    public String encode(
            MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters) {
        logMethodBegin(messageHeaderParameters, payloadParameters);

        if (null == messageHeaderParameters || null == payloadParameters) {
            throw new IllegalArgumentException("Parameters cannot be NULL");
        }
        messageHeaderParameters.validate();
        payloadParameters.validate();

        try (var streamedMessage = new ByteArrayOutputStream()) {

            getNativeLogger().trace("Encode header.");
            header(messageHeaderParameters).writeDelimitedTo(streamedMessage);

            getNativeLogger().trace("Encode payload.");
            payload(payloadParameters).writeDelimitedTo(streamedMessage);

            getNativeLogger().trace("Encoding message.");
            var encodedMessage = Base64.getEncoder().encodeToString(streamedMessage.toByteArray());

            logMethodEnd(encodedMessage);
            return encodedMessage;
        } catch (IOException e) {
            throw new CouldNotEncodeMessageException(e);
        }
    }

    /**
     * Encode a number of messages.
     *
     * @param messageParameterTuples -
     * @return -
     */
    public List<String> encode(List<MessageParameterTuple> messageParameterTuples) {
        return messageParameterTuples.stream()
                .map(
                        messageParameterTuple ->
                                encode(
                                        messageParameterTuple.getMessageHeaderParameters(),
                                        messageParameterTuple.getPayloadParameters()))
                .collect(Collectors.toList());
    }

    /**
     * Chunk and add the Base64 encoding for a message if necessary. If there is only one chunk, the
     * single chunk will be returned as Base64 encoded value. The chunk information and all IDs will
     * be set by the SDK and are no longer in control of the application.
     *
     * @param messageHeaderParameters -
     * @param payloadParameters       Content of the message. It shall not be Base64 encoded before.
     * @return -
     */
    public List<MessageParameterTuple> chunkAndBase64EncodeEachChunk(
            MessageHeaderParameters messageHeaderParameters,
            PayloadParameters payloadParameters,
            OnboardingResponse onboardingResponse) {
        logMethodBegin(messageHeaderParameters, payloadParameters);

        if (null == messageHeaderParameters
                || null == payloadParameters
                || null == onboardingResponse) {
            throw new IllegalArgumentException("Parameters cannot be NULL");
        }
        messageHeaderParameters.validate();
        payloadParameters.validate();

        if (messageHeaderParameters
                .getTechnicalMessageType()
                .needsBase64EncodingAndHasToBeChunkedIfNecessary()) {
            if (payloadParameters.shouldBeChunked()) {
                getNativeLogger()
                        .debug(
                                "The message should be chunked, current size of the payload ({}) is above the limitation.",
                                payloadParameters.getValue().toStringUtf8().length());
                var wholeMessage = payloadParameters.getValue().toByteArray();
                final var messageChunks = splitIntoChunks(wholeMessage);
                List<MessageParameterTuple> tuples = new ArrayList<>();
                var chunkNr = new AtomicInteger(1);
                final var chunkContextId = ChunkContextIdService.generateChunkContextId();
                messageChunks.forEach(
                        chunk -> {
                            final var messageIdForChunk = MessageIdService.generateMessageId();
                            final var sequenceNumberForChunk =
                                    SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse);

                            final var header = new MessageHeaderParameters();
                            header.copy(messageHeaderParameters);
                            header.setApplicationMessageId(messageIdForChunk);
                            header.setApplicationMessageSeqNo(sequenceNumberForChunk);
                            var chunkInfo = Chunk.ChunkComponent.newBuilder();
                            chunkInfo.setContextId(chunkContextId);
                            chunkInfo.setCurrent(chunkNr.getAndIncrement());
                            chunkInfo.setTotal(messageChunks.size());
                            chunkInfo.setTotalSize(wholeMessage.length);
                            header.setChunkInfo(chunkInfo.build());

                            final var payload = new PayloadParameters();
                            payload.copyFrom(payloadParameters);
                            payload.setValue(ByteString.copyFromUtf8(Base64.getEncoder().encodeToString(chunk)));

                            tuples.add(new MessageParameterTuple(header, payload));
                        });
                return tuples;
            } else {
                getNativeLogger()
                        .debug(
                                "The message is not chunked since the current size of the payload ({}) is not above the limitation and the technical message type '{}' does support chunking.",
                                payloadParameters.getValue().toStringUtf8().length(),
                                messageHeaderParameters.getTechnicalMessageType().getKey());
                getNativeLogger()
                        .debug("The content is encoded, since in other cases the content is encoded as well.");
                final var payload = new PayloadParameters();
                payload.copyFrom(payloadParameters);
                payload.setValue(
                        ByteString.copyFromUtf8(
                                Base64.getEncoder()
                                        .encodeToString(
                                                payloadParameters
                                                        .getValue()
                                                        .toByteArray())));
                return Collections.singletonList(
                        new MessageParameterTuple(messageHeaderParameters, payload));
            }
        } else {
            getNativeLogger()
                    .debug(
                            "The message type does not need chunking and base 64 encoding, we are returning the tuple 'as it is'.");
            return Collections.singletonList(
                    new MessageParameterTuple(messageHeaderParameters, payloadParameters));
        }
    }

    private List<byte[]> splitIntoChunks(byte[] wholeMessage) {
        List<byte[]> chunks = new ArrayList<>();
        var remainingBytes = wholeMessage;
        do {
            final var chunk =
                    Arrays.copyOfRange(remainingBytes, 0, MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT);
            chunks.add(chunk);
            remainingBytes =
                    Arrays.copyOfRange(
                            remainingBytes, MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT, remainingBytes.length);
        } while (remainingBytes.length > MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT);
        if (remainingBytes.length > 0) {
            chunks.add(remainingBytes);
        }
        return chunks;
    }

    private Request.RequestEnvelope header(MessageHeaderParameters parameters) {
        logMethodBegin(parameters);

        getNativeLogger().trace("Create message header.");
        var messageHeader =
                Request.RequestEnvelope.newBuilder();
        messageHeader.setApplicationMessageId(parameters.getApplicationMessageId());
        messageHeader.setApplicationMessageSeqNo(parameters.getApplicationMessageSeqNo());
        messageHeader.setTechnicalMessageType(parameters.getTechnicalMessageType().getKey());
        messageHeader.setMode(parameters.getMode());
        if (null != parameters.getMetadata()) {
            messageHeader.setMetadata(parameters.getMetadata());
        }
        if (StringUtils.isNotBlank(parameters.getTeamSetContextId())) {
            messageHeader.setTeamSetContextId(parameters.getTeamSetContextId());
        }
        if (!parameters.getRecipients().isEmpty()) {
            messageHeader.addAllRecipients(parameters.getRecipients());
        }
        if (parameters.getChunkInfo() != null) {
            messageHeader.setChunkInfo(parameters.getChunkInfo());
        }
        messageHeader.setTimestamp(new TimestampUtil().current());

        getNativeLogger().trace("Build message envelope.");
        var requestEnvelope = messageHeader.build();

        logMethodEnd(requestEnvelope);
        return requestEnvelope;
    }

    private Request.RequestPayloadWrapper payload(PayloadParameters parameters) {
        logMethodBegin(parameters);

        getNativeLogger().trace("Create message payload.");
        var messagePayload =
                Request.RequestPayloadWrapper.newBuilder();
        var builder = Any.newBuilder();
        builder.setTypeUrl(parameters.getTypeUrl());
        builder.setValue(parameters.getValue());
        messagePayload.setDetails(builder.build());

        getNativeLogger().trace("Message message payload wrapper.");
        var requestPayloadWrapper = messagePayload.build();

        logMethodEnd(requestPayloadWrapper);
        return requestPayloadWrapper;
    }
}
