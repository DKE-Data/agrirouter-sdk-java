package com.dke.data.agrirouter.test.messaging.rest;

import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.DEFAULT_INTERVAL;
import static com.dke.data.agrirouter.impl.messaging.rest.MessageFetcher.MAX_TRIES_BEFORE_FAILURE;

import agrirouter.request.Request;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.inner.Message;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.MessageHeaderParameters;
import com.dke.data.agrirouter.api.service.parameters.MessageParameterTuple;
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
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

/** Test case to show the behavior for chunked message sending. */
class SendChunkedMessageTest extends AbstractIntegrationTest {

  public static final int EXPECTED_NUMBER_OF_CHUNKS = 3;

  @Test
  void
      givenLargeContentMessageWhenSendingTheMessageToTheAgrirouterTheSdkShouldHelpToSendTheFileInMultipleChunks()
          throws IOException, InterruptedException {

    final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();
    final SendMessageServiceImpl sendMessageService = new SendMessageServiceImpl();
    final OnboardingResponse onboardingResponse =
        OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);

    MessageHeaderParameters messageHeaderParameters = new MessageHeaderParameters();
    messageHeaderParameters.setTechnicalMessageType(TechnicalMessageType.ISO_11783_TASKDATA_ZIP);
    messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
    messageHeaderParameters.setApplicationMessageSeqNo(
        SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
    messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.PUBLISH);

    PayloadParameters payloadParameters = new PayloadParameters();
    payloadParameters.setValue(fakeLargeMessageContent());
    payloadParameters.setTypeUrl(TechnicalMessageType.EMPTY.getKey());

    List<MessageParameterTuple> tuples =
        encodeMessageService.chunk(messageHeaderParameters, payloadParameters, onboardingResponse);

    List<String> encodedMessages = encodeMessageService.encode(tuples);

    SendMessageParameters sendMessageParameters = new SendMessageParameters();
    sendMessageParameters.setEncodedMessages(encodedMessages);
    sendMessageParameters.setOnboardingResponse(onboardingResponse);
    sendMessageService.send(sendMessageParameters);

    Thread.sleep(TimeUnit.SECONDS.toMillis(3));

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
  private ByteString fakeLargeMessageContent() {
    return ByteString.copyFromUtf8(
        RandomStringUtils.randomAlphabetic(1024000 * EXPECTED_NUMBER_OF_CHUNKS));
  }
}
