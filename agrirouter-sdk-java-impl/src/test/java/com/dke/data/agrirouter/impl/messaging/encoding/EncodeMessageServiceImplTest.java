package com.dke.data.agrirouter.impl.messaging.encoding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageParameterTuple;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.google.protobuf.ByteString;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncodeMessageServiceImplTest {

  @Test
  void givenEmptyMessageWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8("");
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    final List<MessageParameterTuple> chunks =
        encodeMessageService.chunk(
            messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
    Assertions.assertEquals(1, chunks.size());
  }

  @Test
  void
      givenSingleChunkMessageWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8("secretMessage");
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    final List<MessageParameterTuple> chunks =
        encodeMessageService.chunk(
            messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
    Assertions.assertEquals(1, chunks.size());
  }

  @Test
  void
      givenSingleChunkMessageWithMaxSizeWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8(RandomStringUtils.randomAlphabetic(1024000));
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    final List<MessageParameterTuple> chunks =
        encodeMessageService.chunk(
            messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
    Assertions.assertEquals(1, chunks.size());
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void
      givenMultipleChunkMessageWithMaxSizeWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
    EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

    ByteString toSendMessage = ByteString.copyFromUtf8(RandomStringUtils.randomAlphabetic(1024001));
    MessageHeaderParameters messageHeaderParameters = getMessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    PayloadParameters payloadParameters = getPayloadParameters(toSendMessage);

    final List<MessageParameterTuple> chunks =
        encodeMessageService.chunk(
            messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
    Assertions.assertEquals(2, chunks.size());

    Assertions.assertEquals(
        2, chunks.get(0).getMessageHeaderParameters().getChunkInfo().getTotal());
    Assertions.assertEquals(
        2, chunks.get(1).getMessageHeaderParameters().getChunkInfo().getTotal());

    Assertions.assertEquals(
        1, chunks.get(0).getMessageHeaderParameters().getChunkInfo().getCurrent());
    Assertions.assertEquals(
        2, chunks.get(1).getMessageHeaderParameters().getChunkInfo().getCurrent());

    Assertions.assertEquals(
        chunks.get(0).getMessageHeaderParameters().getChunkInfo().getContextId(),
        chunks.get(1).getMessageHeaderParameters().getChunkInfo().getContextId());
  }

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
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.DKE_CAPABILITIES);
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

  private OnboardingResponse fakeOnboardingResponse() {
    OnboardingResponse onboardingResponse = new OnboardingResponse();
    onboardingResponse.setSensorAlternateId("THIS_IS_FAKE");
    return onboardingResponse;
  }
}
