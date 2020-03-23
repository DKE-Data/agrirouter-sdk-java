package com.dke.data.agrirouter.impl.messaging.rest;

import agrirouter.commons.Chunk;
import com.dke.data.agrirouter.api.service.messaging.SendContentMessageService;
import com.dke.data.agrirouter.api.service.messaging.SendMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendContentMessageParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.common.SequenceNumberService;
import com.dke.data.agrirouter.impl.messaging.MessageEncoder;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.validation.ResponseValidator;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SendContentMessageServiceImpl
    implements SendContentMessageService, MessageSender, ResponseValidator, MessageEncoder {

  private final EncodeMessageService encodeMessageService;
  private final SendMessageService sendMessageService;

  public SendContentMessageServiceImpl() {
    this.encodeMessageService = new EncodeMessageServiceImpl();
    this.sendMessageService = new SendMessageServiceImpl();
  }

  @Override
  public String send(SendContentMessageParameters parameters) {
    parameters.validate();

    String base64EncodedMessageContent = Base64.getEncoder().encodeToString(Objects.requireNonNull(parameters.getBase64EncodedMessageContent()).getBytes());

    String applicationMessageId =
        parameters.getApplicationMessageId() != null
            ? parameters.getApplicationMessageId()
            : MessageIdService.generateMessageId();

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setOnboardingResponse(parameters.getOnboardingResponse());
    sendMessageParameters.setApplicationMessageId(applicationMessageId);
    sendMessageParameters.setTeamsetContextId(parameters.getTeamsetContextId());

    if (this.hasMessageContentToBeChunked(base64EncodedMessageContent)) {
      String chunkContextId = UUID.randomUUID().toString();
      int totalSize =
          parameters.getBase64EncodedMessageContent().getBytes(StandardCharsets.UTF_8).length;
      List<byte[]> chunks =
          this.chunkMessageContent(base64EncodedMessageContent, parameters.getChunkSize());
      int current = 0;
      chunks.forEach(
          chunk -> {
            MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
            messageHeaderParameters.setMode(parameters.getMode());
            messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
            messageHeaderParameters.setApplicationMessageSeqNo(SequenceNumberService.next());
            messageHeaderParameters.setTechnicalMessageType(parameters.getTechnicalMessageType());
            messageHeaderParameters.setTeamSetContextId(parameters.getTeamsetContextId());
            messageHeaderParameters.setRecipients(parameters.getRecipients());
            messageHeaderParameters.setMetadata(parameters.getMetadata());
            Chunk.ChunkComponent.Builder chunkInfo = Chunk.ChunkComponent.newBuilder();
            chunkInfo.setContextId(chunkContextId);
            chunkInfo.setCurrent(current);
            chunkInfo.setTotal(chunks.size());
            chunkInfo.setTotalSize(totalSize);
            messageHeaderParameters.setChunkInfo(chunkInfo.build());
            PayloadParameters payloadParameters = new PayloadParameters();
            payloadParameters.setTypeUrl(parameters.getTypeUrl());
            payloadParameters.setValue(ByteString.copyFrom(chunk));
            sendMessageParameters
                .getEncodedMessages()
                .add(this.encodeMessageService.encode(messageHeaderParameters, payloadParameters));
          });
    }

    MessageSenderResponse response = this.sendMessage(sendMessageParameters);
    this.assertStatusCodeIsOk(response.getNativeResponse().getStatus());
    return applicationMessageId;
  }

  private boolean hasMessageContentToBeChunked(String base64EncodedMessageContent) {
    byte[] bytes = base64EncodedMessageContent.getBytes(StandardCharsets.UTF_8);
    int byteCount = bytes.length;
    return byteCount / MAXIMUM_SUPPORTED > 1;
  }

  private List<byte[]> chunkMessageContent(
      String base64EncodedMessageContent, int definedChunkSize) {
    List<byte[]> chunks = new ArrayList<>();
    byte[] messageContent = base64EncodedMessageContent.getBytes(StandardCharsets.UTF_8);
    int chunkSize;
    if (definedChunkSize < 1 || definedChunkSize > MAXIMUM_SUPPORTED) {
      chunkSize = MAXIMUM_SUPPORTED;
    } else {
      chunkSize = definedChunkSize;
    }
    for (int i = 0; i < messageContent.length; i += chunkSize) {
      byte[] chunk =
          Arrays.copyOfRange(messageContent, i, Math.min(messageContent.length, i + chunkSize));
      chunks.add(chunk);
    }
    return chunks;
  }

  @Override
  public EncodeMessageService getEncodeMessageService() {
    return this.encodeMessageService;
  }
}
