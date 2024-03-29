package com.dke.data.agrirouter.api.service.messaging.http;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;

@SuppressWarnings("unused")
public interface MessageQueryService
        extends MessagingService<MessageQueryParameters>,
        MessageDecoder<FeedResponse.MessageQueryResponse> {

    /**
     * Query all messages as default function. The query will be based on a time period since message
     * ID filtering or sender filtering can be achieved using the default message sending.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    String sendMessageToQueryAll(OnboardingResponse onboardingResponse);

    /**
     * Query all messages as async default function. The query will be based on a time period since
     * message ID filtering or sender filtering can be achieved using the default message sending.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    HttpAsyncMessageSendingResult sendMessageToQueryAllAsync(OnboardingResponse onboardingResponse);
}
