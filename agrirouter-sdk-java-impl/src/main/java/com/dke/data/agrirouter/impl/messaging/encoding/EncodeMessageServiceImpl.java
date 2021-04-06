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
import com.google.common.base.Splitter;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
@SuppressWarnings("ConstantConditions")
public class EncodeMessageServiceImpl extends NonEnvironmentalService
    implements EncodeMessageService {

  /**
   * Encode a message. In this case chunking will be done by the application and not by the SDK.
   *
   * @param messageHeaderParameters -
   * @param payloadParameters -
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

    try (ByteArrayOutputStream streamedMessage = new ByteArrayOutputStream()) {

      getNativeLogger().trace("Encode header.");
      header(messageHeaderParameters).writeDelimitedTo(streamedMessage);

      getNativeLogger().trace("Encode payload.");
      payload(payloadParameters).writeDelimitedTo(streamedMessage);

      getNativeLogger().trace("Encoding message.");
      String encodedMessage = Base64.getEncoder().encodeToString(streamedMessage.toByteArray());

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
   * Chunk a message if necessary. The chunk information and all IDs will be set by the SDK and are
   * not longer in control of the application.
   *
   * @param messageHeaderParameters -
   * @param payloadParameters -
   * @return -
   */
  public List<MessageParameterTuple> chunk(
      MessageHeaderParameters messageHeaderParameters,
      PayloadParameters payloadParameters,
      OnboardingResponse onboardingResponse) {
    logMethodBegin(messageHeaderParameters, payloadParameters);

    if (null == messageHeaderParameters || null == payloadParameters) {
      throw new IllegalArgumentException("Parameters cannot be NULL");
    }
    messageHeaderParameters.validate();
    payloadParameters.validate();

    if (null != messageHeaderParameters.getTechnicalMessageType()
        && messageHeaderParameters.getTechnicalMessageType().getNeedsChunking()
        && payloadParameters.shouldBeChunked()) {
      getNativeLogger()
          .debug(
              "The message should be chunked, current size of the payload ({}) is above the limitation.",
              payloadParameters.getValue().toStringUtf8().length());
      String wholeMessage = payloadParameters.getValue().toStringUtf8();
      final List<String> messageChunks =
          Splitter.fixedLength(payloadParameters.maxLengthForMessages()).splitToList(wholeMessage);
      List<MessageParameterTuple> tuples = new ArrayList<>();
      AtomicInteger chunkNr = new AtomicInteger(1);
      final String chunkContextId = ChunkContextIdService.generateChunkContextId();
      messageChunks.forEach(
          chunk -> {
            final String messageIdForChunk = MessageIdService.generateMessageId();
            final long sequenceNumberForChunk =
                SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse);

            final MessageHeaderParameters header = new MessageHeaderParameters();
            header.copy(messageHeaderParameters);
            header.setApplicationMessageId(messageIdForChunk);
            header.setApplicationMessageSeqNo(sequenceNumberForChunk);
            Chunk.ChunkComponent.Builder chunkInfo = Chunk.ChunkComponent.newBuilder();
            chunkInfo.setContextId(chunkContextId);
            chunkInfo.setCurrent(chunkNr.get());
            chunkInfo.setTotal(messageChunks.size());
            chunkInfo.setTotalSize(wholeMessage.length());
            header.setChunkInfo(chunkInfo.build());

            final PayloadParameters payload = new PayloadParameters();
            payload.copy(payload);
            payload.setValue(ByteString.copyFromUtf8(chunk));

            tuples.add(new MessageParameterTuple(header, payload));

            chunkNr.getAndIncrement();
          });
      return tuples;
    } else {
      getNativeLogger()
          .debug(
              "The message is not chunked since the current size of the payload ({}) is not above the limitation or the technical message type '{}' does not support chunking.",
              payloadParameters.getValue().toStringUtf8().length(),
              messageHeaderParameters.getTechnicalMessageType().getKey());
      return Collections.singletonList(
          new MessageParameterTuple(messageHeaderParameters, payloadParameters));
    }
  }

  private Request.RequestEnvelope header(MessageHeaderParameters parameters) {
    logMethodBegin(parameters);

    getNativeLogger().trace("Create message header.");
    agrirouter.request.Request.RequestEnvelope.Builder messageHeader =
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
    Request.RequestEnvelope requestEnvelope = messageHeader.build();

    logMethodEnd(requestEnvelope);
    return requestEnvelope;
  }

  private Request.RequestPayloadWrapper payload(PayloadParameters parameters) {
    logMethodBegin(parameters);

    getNativeLogger().trace("Create message payload.");
    Request.RequestPayloadWrapper.Builder messagePayload =
        Request.RequestPayloadWrapper.newBuilder();
    Any.Builder builder = Any.newBuilder();
    builder.setTypeUrl(parameters.getTypeUrl());
    builder.setValue(parameters.getValue());
    messagePayload.setDetails(builder.build());

    getNativeLogger().trace("Message message payload wrapper.");
    Request.RequestPayloadWrapper requestPayloadWrapper = messagePayload.build();

    logMethodEnd(requestPayloadWrapper);
    return requestPayloadWrapper;
  }
}
