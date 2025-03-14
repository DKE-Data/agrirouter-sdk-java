package com.dke.data.agrirouter.test.messaging.rest;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.PayloadParameters;
import com.dke.data.agrirouter.api.service.parameters.SendMessageParameters;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.SequenceNumberService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SendMessageServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import com.dke.data.agrirouter.test.helper.ContentReader;
import com.google.protobuf.ByteString;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Test case to show the behavior for chunked message sending.
 */
class SendChunkedMessageTest extends AbstractIntegrationTest {

    private static final int MAX_CHUNK_SIZE = 1024000;

    @Test
    @Disabled("Issue #197 | Needs investigation, not running any longer.")
    void
    givenLargeContentMessageWhenSendingTheMessageToTheAgrirouterTheSdkShouldHelpToSendTheFileInMultipleChunks()
            throws Throwable {

        final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();
        final var sendMessageService = new SendMessageServiceImpl();
        final var onboardingResponse =
                OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);

        var messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
        messageHeaderParameters.setApplicationMessageSeqNo(
                SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
        messageHeaderParameters.setRecipients(
                Collections.singletonList("797b7f4b-79ec-4247-9fba-e726a55c4c7f"));

        var payloadParameters = new PayloadParameters();
        payloadParameters.setValue(
                ByteString.copyFrom(ContentReader.readRawData(ContentReader.Identifier.BIG_TASK_DATA)));
        payloadParameters.setTypeUrl(SystemMessageType.EMPTY.getKey());

        // create message tuples for each chunk
        var tuples =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
                        messageHeaderParameters, payloadParameters, onboardingResponse);

        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(onboardingResponse);

        // encode and send each chunk
        tuples.forEach(
                messageParameterTuple -> {
                    Assertions.assertTrue(
                            Objects.requireNonNull(messageParameterTuple.getPayloadParameters().getValue())
                                    .toStringUtf8()
                                    .length()
                                    <= MAX_CHUNK_SIZE);
                    String encodedMessage = encodeMessageService.encode(
                            messageParameterTuple.getMessageHeaderParameters(),
                            messageParameterTuple.getPayloadParameters());
                    sendMessageParameters.setEncodedMessages(Collections.singletonList(encodedMessage));
                    sendMessageService.send(sendMessageParameters);
                }
        );

        waitForTheAgrirouterToProcessMultipleMessages();

        // WARNING: receiving part might not be accurate for now, as we changed the sending structure from
        // sending all chunks in _one_ agrirouter message (which was incorrect) to sending each chunk in a
        // dedicated message

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        onboardingResponse,
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(3, fetchMessageResponses.get().size());

        DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        var decodeMessageResponse = new AtomicReference<DecodeMessageResponse>();
        fetchMessageResponses.get().stream()
                .map(FetchMessageResponse::getCommand)
                .forEach(
                        message -> {
                            Assertions.assertNotNull(message);
                            decodeMessageResponse.set(decodeMessageService.decode(message.getMessage()));

                            Assertions.assertMatchesAny(
                                    Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
                                    decodeMessageResponse.get().getResponseEnvelope().getResponseCode());
                        });
    }
}
