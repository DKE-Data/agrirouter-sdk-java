package com.dke.data.agrirouter.test.usecase;

import agrirouter.feed.response.FeedResponse;
import agrirouter.request.Request;
import agrirouter.response.Response;
import com.dke.data.agrirouter.api.cancellation.DefaultCancellationToken;
import com.dke.data.agrirouter.api.dto.encoding.DecodeMessageResponse;
import com.dke.data.agrirouter.api.dto.messaging.FetchMessageResponse;
import com.dke.data.agrirouter.api.enums.ContentMessageType;
import com.dke.data.agrirouter.api.enums.SystemMessageType;
import com.dke.data.agrirouter.api.env.QA;
import com.dke.data.agrirouter.api.service.messaging.encoding.DecodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.encoding.EncodeMessageService;
import com.dke.data.agrirouter.api.service.messaging.http.FetchMessageService;
import com.dke.data.agrirouter.api.service.parameters.*;
import com.dke.data.agrirouter.impl.common.MessageIdService;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import com.dke.data.agrirouter.impl.messaging.SequenceNumberService;
import com.dke.data.agrirouter.impl.messaging.encoding.DecodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.encoding.EncodeMessageServiceImpl;
import com.dke.data.agrirouter.impl.messaging.rest.*;
import com.dke.data.agrirouter.test.AbstractIntegrationTest;
import com.dke.data.agrirouter.test.Assertions;
import com.dke.data.agrirouter.test.OnboardingResponseRepository;
import com.dke.data.agrirouter.test.helper.ContentReader;
import com.google.protobuf.ByteString;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test case to show the behavior for chunked message sending.
 */
class SendAndReceiveChunkedMessagesTest extends AbstractIntegrationTest {

    private static final int MAX_CHUNK_SIZE = 1024000;

    @Disabled("Takes to much during a normal run.")
    @ParameterizedTest
    @MethodSource
    void givenRealMessageContentWhenSendingMessagesTheContentShouldMatchAfterReceivingAndMergingIt(
            ByteString messageContent, int expectedNrOfChunks) throws Throwable {
        actionsForSender(messageContent, expectedNrOfChunks);
        actionsForTheRecipient(expectedNrOfChunks);
    }

    /**
     * These are the actions for the recipient. The recipient is already set up and declared the
     * capabilities. The actions for the recipient are documented inline.
     *
     * @param expectedNrOfChunks -
     */
    private void actionsForTheRecipient(int expectedNrOfChunks)
            throws Throwable {
        // [1] Fetch all the messages within the feed. The number of headers should match the number of
        // chunks sent.
        final var messageQueryService = new MessageQueryServiceImpl(communicationUnit.getEnvironment());
        final var messageQueryParameters = new MessageQueryParameters();
        final var recipient =
                OnboardingResponseRepository.read(
                        OnboardingResponseRepository.Identifier.COMMUNICATION_UNIT);
        messageQueryParameters.setOnboardingResponse(recipient);
        messageQueryParameters.setSentToInSeconds(UtcTimeService.inTheFuture(5).toEpochSecond());
        messageQueryParameters.setSentFromInSeconds(
                UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
        messageQueryService.send(messageQueryParameters);

        // [2] Wait for the agrirouter to process the message.
        waitForTheAgrirouterToProcessSingleMessage();

        // [3] Fetch the chunks from the outbox. Since we have the same restrictions while receiving,
        // this has to be the same number of messages as it is chunks.
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(expectedNrOfChunks, fetchMessageResponses.get().size());

        // [4] Check if the response from the AR only contains valid results.
        final DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        fetchMessageResponses.get().stream()
                .map(
                        fetchMessageResponse ->
                                decodeMessageService.decode(fetchMessageResponse.getCommand().getMessage()))
                .forEach(
                        decodeMessageResponse ->
                                Assertions.assertEquals(
                                        Response.ResponseEnvelope.ResponseBodyType.ACK_FOR_FEED_MESSAGE,
                                        decodeMessageResponse.getResponseEnvelope().getType()));

        // [5] Map the results from the query to 'real' messages within the feed and perform some
        // assertions.
        final var feedMessages =
                fetchMessageResponses.get().stream()
                        .map(
                                fetchMessageResponse ->
                                        decodeMessageService.decode(fetchMessageResponse.getCommand().getMessage()))
                        .map(
                                decodeMessageResponse ->
                                        messageQueryService.decode(
                                                decodeMessageResponse.getResponsePayloadWrapper().getDetails().getValue()))
                        .map(messageQueryResponse -> messageQueryResponse.getMessages(0))
                        .toList();
        Assertions.assertEquals(expectedNrOfChunks, feedMessages.size());
        feedMessages.forEach(
                feedMessage -> Assertions.assertNotNull(feedMessage.getHeader().getChunkContext()));
        Assertions.assertEquals(
                feedMessages.get(0).getHeader().getChunkContext().getTotal(), expectedNrOfChunks);
        final var chunkContextIds =
                feedMessages.stream()
                        .map(feedMessage -> feedMessage.getHeader().getChunkContext().getContextId())
                        .collect(Collectors.toSet());
        Assertions.assertEquals(
                1, chunkContextIds.size(), "There should be only one chunk context ID.");

        // [6] Confirm the chunks to remove them from the feed.
        final var messageIdsToConfirm =
                feedMessages.stream()
                        .map(feedMessage -> feedMessage.getHeader().getMessageId())
                        .collect(Collectors.toList());
        final var messageConfirmationService =
                new MessageConfirmationServiceImpl(communicationUnit.getEnvironment());
        final var messageConfirmationParameters =
                new MessageConfirmationParameters();
        messageConfirmationParameters.setOnboardingResponse(recipient);
        messageConfirmationParameters.setMessageIds(messageIdsToConfirm);
        messageConfirmationService.send(messageConfirmationParameters);

        // [7] Fetch the response from the agrirouter after confirming the messages.
        waitForTheAgrirouterToProcessSingleMessage();
        fetchMessageResponses =
                fetchMessageService.fetch(
                        recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(
                1, fetchMessageResponses.get().size(), "This should be a single response.");
    }

    /**
     * These are the actions for the sender. The sender is already set up and declared the
     * capabilities. The actions for the sender are documented inline.
     *
     * @param messageContent     -
     * @param expectedNrOfChunks -
     * @throws IOException          -
     * @throws InterruptedException -
     */
    private void actionsForSender(ByteString messageContent, int expectedNrOfChunks)
            throws IOException, InterruptedException {
        final EncodeMessageService encodeMessageService = new EncodeMessageServiceImpl();
        final var sendMessageService = new SendMessageServiceImpl();
        final var onboardingResponse =
                OnboardingResponseRepository.read(OnboardingResponseRepository.Identifier.FARMING_SOFTWARE);

        // [1] Define the raw message, in this case this is the Base64 encoded message content, no
        // chunking needed.
        var messageHeaderParameters = new MessageHeaderParameters();
        messageHeaderParameters.setTechnicalMessageType(ContentMessageType.ISO_11783_TASKDATA_ZIP);
        messageHeaderParameters.setApplicationMessageId(MessageIdService.generateMessageId());
        messageHeaderParameters.setApplicationMessageSeqNo(
                SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
        messageHeaderParameters.setMode(Request.RequestEnvelope.Mode.DIRECT);
        messageHeaderParameters.setRecipients(
                Collections.singletonList(
                        OnboardingResponseRepository.read(
                                        OnboardingResponseRepository.Identifier.COMMUNICATION_UNIT)
                                .getSensorAlternateId()));

        var payloadParameters = new PayloadParameters();
        payloadParameters.setValue(messageContent);
        payloadParameters.setTypeUrl(SystemMessageType.EMPTY.getKey());

        // [2] Chunk the message content using the SDK specific methods ('chunkAndEncode').
        var tuples =
                encodeMessageService.chunkAndBase64EncodeEachChunk(
                        messageHeaderParameters, payloadParameters, onboardingResponse);

        // [3] Send each chunk to the agrirouter.
        var sendMessageParameters = new SendMessageParameters();
        sendMessageParameters.setOnboardingResponse(onboardingResponse);

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

        // [4] Wait for the AR to process the chunks.
        waitForTheAgrirouterToProcessMultipleMessages();

        // [5] Check if the chunks were processed successfully.

        // WARNING: receiving part might not be accurate for now, as we changed the sending structure from
        // sending all chunks in _one_ agrirouter message (which was incorrect) to sending each chunk in a
        // dedicated message

        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        onboardingResponse,
                        new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(expectedNrOfChunks, fetchMessageResponses.get().size());

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

    /**
     * Delivers fake message content for multiple test cases.
     *
     * @return -
     */
    @SuppressWarnings("unused")
    private static @NotNull Stream<Arguments>
    givenRealMessageContentWhenSendingMessagesTheContentShouldMatchAfterReceivingAndMergingIt()
            throws Throwable {
        return Stream.of(
                Arguments.of(
                        ByteString.copyFrom(ContentReader.readRawData(ContentReader.Identifier.BIG_TASK_DATA)),
                        3));
    }

    /**
     * Cleanup before and after each test case. These actions are necessary because it could be the
     * case, that there are dangling messages from former tests.
     */
    @BeforeEach
    @AfterEach
    public void prepareTestEnvironment() throws Throwable {
        FetchMessageService fetchMessageService = new FetchMessageServiceImpl();
        final var recipient =
                OnboardingResponseRepository.read(
                        OnboardingResponseRepository.Identifier.COMMUNICATION_UNIT);
        final var messageHeaderQueryService =
                new MessageHeaderQueryServiceImpl(communicationUnit.getEnvironment());

        // [1] Clean the outbox of the endpoint.
        fetchMessageService.fetch(
                recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));

        // [2] Fetch all message headers for the last 4 weeks (maximum retention time within the
        // agrirouter).
        final var messageQueryParameters = new MessageQueryParameters();
        messageQueryParameters.setOnboardingResponse(recipient);
        messageQueryParameters.setSentToInSeconds(UtcTimeService.inTheFuture(5).toEpochSecond());
        messageQueryParameters.setSentFromInSeconds(
                UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
        messageHeaderQueryService.send(messageQueryParameters);
        waitForTheAgrirouterToProcessSingleMessage();
        var fetchMessageResponses =
                fetchMessageService.fetch(
                        recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
        Assertions.assertTrue(fetchMessageResponses.isPresent());
        Assertions.assertEquals(
                1, fetchMessageResponses.get().size(), "This should be a single response.");
        final DecodeMessageService decodeMessageService = new DecodeMessageServiceImpl();
        final var decodeMessageResponse =
                decodeMessageService.decode(fetchMessageResponses.get().get(0).getCommand().getMessage());
        Assertions.assertEquals(
                Response.ResponseEnvelope.ResponseBodyType.ACK_FOR_FEED_HEADER_LIST,
                decodeMessageResponse.getResponseEnvelope().getType());
        final var headerQueryResponse =
                messageHeaderQueryService.decode(
                        decodeMessageResponse.getResponsePayloadWrapper().getDetails().getValue());

        // [3] Delete the dangling messages from the feed of the endpoint if necessary.
        if (headerQueryResponse.getQueryMetrics().getTotalMessagesInQuery() > 0) {
            final var deleteMessageService = new DeleteMessageServiceImpl();
            final var deleteMessageParameters = new DeleteMessageParameters();
            deleteMessageParameters.setOnboardingResponse(recipient);
            final var messageIds =
                    headerQueryResponse.getFeedList().stream()
                            .map(FeedResponse.HeaderQueryResponse.Feed::getHeadersList)
                            .flatMap(Collection::stream)
                            .map(FeedResponse.HeaderQueryResponse.Header::getMessageId)
                            .collect(Collectors.toList());
            deleteMessageParameters.setMessageIds(messageIds);
            deleteMessageService.send(deleteMessageParameters);
            waitForTheAgrirouterToProcessSingleMessage();
            fetchMessageService.fetch(
                    recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
        }

        // [4] Clean the outbox of the endpoint.
        fetchMessageService.fetch(
                recipient, new DefaultCancellationToken(MAX_TRIES_BEFORE_FAILURE, DEFAULT_INTERVAL));
    }
}
