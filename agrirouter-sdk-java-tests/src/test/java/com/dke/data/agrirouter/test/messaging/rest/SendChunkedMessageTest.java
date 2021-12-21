package com.dke.data.agrirouter.test.messaging.rest;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.messaging.SequenceNumberService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.FetchMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.SendMessageServiceImpl;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Test case to show the behavior for chunked message sending. */
class SendChunkedMessageTest extends AbstractIntegrationTest {

  private static final int MAX_CHUNK_SIZE = 1024000;

  @ParameterizedTest
  @MethodSource("fakeRawMessageContentThatHasToBeChunked")
  void
      givenLargeContentMessageWhenSendingTheMessageToTheAgrirouterTheSdkShouldHelpToSendTheFileInMultipleChunks(
          ByteString messageContent, int expectedNrOfChunks)
          throws IOException, InterruptedException {

    final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();
    final SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    final OnboardingResponse onboardingResponse =
        OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
    messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
    messageHeaderParameters.setApplicationMessageSeqNo(
        SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.PUBLISH);

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setValue(messageContent);
    payloadParameters.setTypeUrl(SystemMessageType.EMPTY.getKey());

    List<MessageParameterTuple> tuples =
        encodeMessageService.chunkAndEncode(
            messageHeaderParameters, payloadParameters, onboardingResponse);

    tuples.forEach(
        messageParameterTuple ->
            Assertions.assertTrue(
                Objects.requireNonNull(messageParameterTuple.getPayloadParameters().getValue())
                        .toStringUtf8()
                        .length()
                    <= MAX_CHUNK_SIZE));

    List<String> encodedMessages = encodeMessageService.encode(tuples);

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setEncodedMessages(encodedMessages);
    sendMessageParameters.setOnboardingResponse(onboardingResponse);
    sendMessageService.send(sendMessageParameters);

    waitForTheAgrirouterToProcessMultipleMessages();

    FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
    Optional<List<FetchMessageResponse>> fetchMessageResponses =
        fetchMessageService.fetch(
            onboardingResponse,
            new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

    Assertions.assertTrue(fetchMessageResponses.isPresent());
    Assertions.assertEquals(expectedNrOfChunks, fetchMessageResponses.get().size());

    DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
    AtomicReference<DecodeMessageResponse> decodeMessageResponse = new AtomicReference<>();
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

  /**
   * Delivers fake message content for multiple test cases.
   *
   * @return -
   */
  private static Stream<Arguments> fakeRawMessageContentThatHasToBeChunked() {
    return Stream.of(
        Arguments.of(
            ByteString.copyFromUtf8(
                RandomStringUtils.randomAlphabetic(
                    PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT * 3)),
            3),
        Arguments.of(
            ByteString.copyFromUtf8(
                RandomStringUtils.randomAlphabetic(
                    PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT * 2)),
            2),
        Arguments.of(
            ByteString.copyFromUtf8(
                RandomStringUtils.randomAlphabetic(
                    PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT)),
            1));
  }
}
