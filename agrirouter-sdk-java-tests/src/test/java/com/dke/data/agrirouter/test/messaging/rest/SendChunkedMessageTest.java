package com.dke.data.agrirouter.test.messaging.rest;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

/** Test case to show the behavior for chunked message sending. */
class SendChunkedMessageTest extends AbstractIntegrationTest {

  private static final int MAX_CHUNK_SIZE = 1024000;
  private static final int EXPECTED_NUMBER_OF_CHUNKS = 3;

  @Test
  void
      givenLargeContentMessageWhenSendingTheMessageToTheAgrirouterTheSdkShouldHelpToSendTheFileInMultipleChunks()
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
    payloadParameters.setValue(fakeRawMessageContentThatHasToBeChunked());
    payloadParameters.setTypeUrl(SystemMessageType.EMPTY.getKey());

    List<MessageParameterTuple> tuples =
        encodeMessageService.chunk(messageHeaderParameters, payloadParameters, onboardingResponse);

    tuples.forEach(messageParameterTuple -> Assertions.assertTrue(Objects.requireNonNull(messageParameterTuple.getPayloadParameters().getValue()).toStringUtf8().length()<MAX_CHUNK_SIZE));

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
    Assertions.assertEquals(EXPECTED_NUMBER_OF_CHUNKS, fetchMessageResponses.get().size());
    Assertions.assertNotNull(fetchMessageResponses.get().get(0).getCommand());
    Assertions.assertNotNull(fetchMessageResponses.get().get(1).getCommand());
    Assertions.assertNotNull(fetchMessageResponses.get().get(2).getCommand());

    Message firstAck = fetchMessageResponses.get().get(0).getCommand();
    Message secondAck = fetchMessageResponses.get().get(1).getCommand();
    Message thirdAck = fetchMessageResponses.get().get(2).getCommand();

    Arrays.stream(new Message[] {firstAck, secondAck, thirdAck})
        .forEach(
            message -> {
              DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
              DecodeMessageResponse decodeMessageResponse =
                  decodeMessageService.decode(message.getMessage());

              Assertions.assertMatchesAny(
                  Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_NO_CONTENT),
                  decodeMessageResponse.getResponseEnvelope().getResponseCode());
            });
  }

  /**
   * Delivers fake message content for three chunks.
   *
   * @return -
   */
  private ByteString fakeRawMessageContentThatHasToBeChunked() {
    return ByteString.copyFromUtf8(
        RandomStringUtils.randomAlphabetic(PayloadParametersKt.MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT * EXPECTED_NUMBER_OF_CHUNKS));
  }
}
