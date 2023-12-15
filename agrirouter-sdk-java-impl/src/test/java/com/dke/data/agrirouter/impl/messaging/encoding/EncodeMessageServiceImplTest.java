package com.dke.data.agrirouter.impl.messaging.encoding;

import agrirouter.request.Request;
import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParametersKt;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AEncodeEncodeMessageServiceImplTest {

    @Test
    void givenEmptyMessageWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var toSendMessage = ByteString.copyFromUtf8("");
        var messageHeaderParameters = getMessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        var payloadParameters = getPayloadParameters(toSendMessage);

        final var chunks =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
                        messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
        Assertions.assertEquals(1, chunks.size());
    }

    @Test
    void
    givenSingleChunkMessageWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var toSendMessage = ByteString.copyFromUtf8("secretMessage");
        var messageHeaderParameters = getMessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        var payloadParameters = getPayloadParameters(toSendMessage);

        final var chunks =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
                        messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
        Assertions.assertEquals(1, chunks.size());
    }

    @Test
    void
    givenSingleChunkMessageWithMaxSizeWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var toSendMessage =
                ByteString.copyFromUtf8(
                        RandomStringUtils.randomAlphabetic(
                                PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT));
        var messageHeaderParameters = getMessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        var payloadParameters = getPayloadParameters(toSendMessage);

        final var chunks =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
                        messageHeaderParameters, payloadParameters, fakeOnboardingResponse());
        Assertions.assertEquals(1, chunks.size());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void
    givenMultipleChunkMessageWithMaxSizeWhenChunkingThenTheImplementationShouldReturnTheRightNumberOfChunks() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var toSendMessage = ByteString.copyFromUtf8(RandomStringUtils.randomAlphabetic(1024001));
        var messageHeaderParameters = getMessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        var payloadParameters = getPayloadParameters(toSendMessage);

        final var chunks =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
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

        var toSendMessage = ByteString.copyFromUtf8("secretMessage");
        var messageHeaderParameters = getMessageHeaderParameters();
        var payloadParameters = getPayloadParameters(toSendMessage);

        var encodedMessage = encodeMessageService.encode(messageHeaderParameters, payloadParameters);
        var decodeMessageService = new DecodeMessageServiceImpl();
        var response = decodeMessageService.decode(encodedMessage);
        Assertions.assertEquals(
                "secretMessage",
                response.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8());
    }

    @Test
    void givenWrongPayloadEncodeAndDecodeBackShouldFail() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var toSendMessage = ByteString.copyFromUtf8("wrong Message");
        var messageHeaderParameters = getMessageHeaderParameters();
        var payloadParameters = getPayloadParameters(toSendMessage);

        var encodedMessage = encodeMessageService.encode(messageHeaderParameters, payloadParameters);
        var decodeMessageService = new DecodeMessageServiceImpl();
        var response = decodeMessageService.decode(encodedMessage);
        Assertions.assertNotEquals(
                "secretMessage",
                response.getResponsePayloadWrapper().getDetails().getValue().toStringUtf8());
    }

    @Test
    void givenNullPayLoadParametersEncodeShouldThrowException() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var messageHeaderParameters = getMessageHeaderParameters();
        assertThrows(
                IllegalArgumentException.class,
                () -> encodeMessageService.encode(messageHeaderParameters, null));
    }

    @Test
    void givenNullMessageHeaderEncodeShouldThrowException() {
        EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();

        var payloadParameters =
                getPayloadParameters(ByteString.copyFromUtf8("secretMessage"));
        assertThrows(
                IllegalArgumentException.class, () -> encodeMessageService.encode(null, payloadParameters));
    }

    private MessageHeaderParameters getMessageHeaderParameters() {
        var messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setApplicationMessageId("1");
        messageHeaderParameters.setApplicationMessageSeqNo(1);
        messageHeaderParameters.setTechnicalMessageType(SystemMessageType.DKE_CAPABILITIES);
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
        return messageHeaderParameters;
    }

    private PayloadParameters getPayloadParameters(ByteString toSendMessage) {
        var payloadParameters = new PayloadParameters();
        payloadParameters.setTypeUrl(
                Capabilities.CapabilitySpecification.getDescriptor().getFullName());
        payloadParameters.setValue(toSendMessage);
        return payloadParameters;
    }

    private OnboardingResponse fakeOnboardingResponse() {
        var onboardingResponse = new OnboardingResponse();
        onboardingResponse.setSensorAlternateId("THIS_IS_FAKE");
        return onboardingResponse;
    }
}
