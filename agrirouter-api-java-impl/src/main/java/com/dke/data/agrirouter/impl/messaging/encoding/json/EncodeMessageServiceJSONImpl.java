package com.dke.data.agrirouter.impl.messaging.encoding.json;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.dto.encoding.EncodeMessageResponse;
import com.dke.data.agrirouter.api.exception.CouldNotEncodeMessageException;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.util.TimestampUtil;
import com.dke.data.agrirouter.impl.NonEnvironmentalService;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.google.protobuf.Any;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/** Internal service implementation. */
public class EncodeMessageServiceJSONImpl extends EncodeMessageServiceImpl {

  @Override
  public EncodeMessageResponse.EncodeMessageResponseJSON encode(
      MessageHeaderParameters messageHeaderParameters, PayloadParameters payloadParameters) {
    this.logMethodBegin(messageHeaderParameters, payloadParameters);

    if (null == messageHeaderParameters || null == payloadParameters) {
      throw new IllegalArgumentException("Parameters cannot be NULL");
    }
    messageHeaderParameters.validate();
    payloadParameters.validate();

    byte[] messageBuffer = encodeStreamedMessage(messageHeaderParameters, payloadParameters);
    String encodedMessage = Base64.getEncoder().encodeToString(messageBuffer);

    this.logMethodEnd(encodedMessage);

    return new EncodeMessageResponse.EncodeMessageResponseJSON(
        messageHeaderParameters.applicationMessageId, encodedMessage);

  }
}
