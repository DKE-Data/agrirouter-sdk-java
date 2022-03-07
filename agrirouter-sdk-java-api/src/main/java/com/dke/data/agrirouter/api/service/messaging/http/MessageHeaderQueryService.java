package com.dke.data.agrirouter.api.service.messaging.http;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;

public interface MessageHeaderQueryService
    extends MessagingService<MessageQueryParameters>,
        MessageDecoder<FeedResponse.HeaderQueryResponse> {

  /**
   * Query all message headers as default function. The query will be based on a time period since
   * message ID filtering or sender filtering can be achieved using the default message sending.
   *
   * @param onboardingResponse The onboard response for the endpoint.
   * @return The message ID.
   */
  String sendMessageToQueryAll(OnboardingResponse onboardingResponse);

  /**
   * Query all message headers as async default function. The query will be based on a time period
   * since message ID filtering or sender filtering can be achieved using the default message
   * sending.
   *
   * @param onboardingResponse The onboard response for the endpoint.
   * @return The message ID.
   */
  HttpAsyncMessageSendingResult sendMessageToQueryAllAsync(OnboardingResponse onboardingResponse);
}
