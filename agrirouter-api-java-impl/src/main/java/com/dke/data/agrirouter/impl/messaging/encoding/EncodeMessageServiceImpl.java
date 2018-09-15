package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.exception.CouldNotEncodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.google.protobuf.Any;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/** Internal service implementation. */
public class EncodeMessageServiceImpl extends NonEnvironmentalService
    implements EncodeMessageService {

  public String encode(
      MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters) {
    this.getNativeLogger().debug("BEGIN | Encode message.");
    this.getNativeLogger()
        .trace(new ObjectArrayMessage(messageHeaderParameters, payloadParameters));

    if (null == messageHeaderParameters || null == payloadParameters) {
      throw new IllegalArgumentException("Parameters cannot be NULL");
    }
    messageHeaderParameters.validate();
    payloadParameters.validate();

    try (ByteArrayOutputStream streamedMessage = new ByteArrayOutputStream()) {

      this.getNativeLogger().trace("Encode header.");
      this.header(messageHeaderParameters).writeDelimitedTo(streamedMessage);

      this.getNativeLogger().trace("Encode payload.");
      this.payload(payloadParameters).writeDelimitedTo(streamedMessage);

      this.getNativeLogger().trace("Encoding message.");
      String encodedMessage = Base64.getEncoder().encodeToString(streamedMessage.toByteArray());

      this.getNativeLogger().trace(new ObjectArrayMessage(encodedMessage));
      this.getNativeLogger().debug("END | Encode message.");
      return encodedMessage;
    } catch (IOException e) {
      throw new CouldNotEncodeMessageException(e);
    }
  }

  private Request.RequestEnvelope header(MessageHeaderParameters parameters) {
    this.getNativeLogger().debug("BEGIN | Encode message header.");
    agrirouter.request.Request.RequestEnvelope.Builder messageHeader =
        Request.RequestEnvelope.newBuilder();
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
    Request.RequestEnvelope requestEnvelope = messageHeader.build();

    this.getNativeLogger().trace(new ObjectArrayMessage(requestEnvelope));
    this.getNativeLogger().debug("END | Encode message header.");
    return requestEnvelope;
  }

  private Request.RequestPayloadWrapper payload(PayloadParameters parameters) {
    this.getNativeLogger().debug("BEGIN | Encode message payload.");
    Request.RequestPayloadWrapper.Builder messagePayload =
        Request.RequestPayloadWrapper.newBuilder();
    Any.Builder builder = Any.newBuilder();
    builder.setTypeUrl(parameters.getTypeUrl());
    builder.setValue(parameters.getValue());
    messagePayload.setDetails(builder.build());
    Request.RequestPayloadWrapper requestPayloadWrapper = messagePayload.build();

    this.getNativeLogger().trace(new ObjectArrayMessage(requestPayloadWrapper));
    this.getNativeLogger().debug("END | Encode message payload.");
    return requestPayloadWrapper;
  }
}
