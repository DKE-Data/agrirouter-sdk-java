package com.dke.data.agrirouter.impl.messaging.encoding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncodeMessageServiceImplTest {

  @Test
  void givenValidParametersEncodeAndDecodeBackShouldNotFail() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8("secretMessage");
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    String encodedMessage = encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    DecodeMessageServiceImpl decodeMessageService = new DecodeMessageServiceImpl();
    DecodeMessageResponse response = decodeMessageService.decode(encodedMessage);
    Assertions.assertEquals(
        "secretMessage",
        response.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8());
  }

  @Test
  void givenWrongPayloadEncodeAndDecodeBackShouldFail() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8("wrong Message");
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    String encodedMessage = encodeMessageService.encode(messageHeaderParameters, payloadParameters);
    DecodeMessageServiceImpl decodeMessageService = new DecodeMessageServiceImpl();
    DecodeMessageResponse response = decodeMessageService.decode(encodedMessage);
    Assertions.assertNotEquals(
        "secretMessage",
        response.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8());
  }

  @Test
  void givenNullPayLoadParametersEncodeShouldThrowException() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    assertThrows(
        IllegalArgumentException.class,
        () -> encodeMessageService.encode(messageHeaderParameters, null));
  }

  @Test
  void givenNullMessageHeaderEncodeShouldThrowException() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    PayloadParameters payloadParameters =
        getPayloadParameters(ByteString.copyFromUtf8("secretMessage"));
    assertThrows(
        IllegalArgumentException.class, () -> encodeMessageService.encode(null, payloadParameters));
  }

  private MessageHeaderParameters getMessageHeaderParameters() {
    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setApplicationMessageId("1");
    messageHeaderParameters.setApplicationMessageSeqNo(1);
    messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_CAPABILITIES);
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
    return messageHeaderParameters;
  }

  private PayloadParameters getPayloadParameters(ByteString toSendMessage) {
    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setTypeUrl(
        Capabilities.CapabilitySpecification.getDescriptor().getFullName());
    payloadParameters.setValue(toSendMessage);
    return payloadParameters;
  }
}
