package com.dke.data.agrirouter.api.service.messaging;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;

public interface MessageHeaderQueryService extends MessagingService<MessageQueryParameters>, MessageDecoder<FeedResponse.HeaderQueryResponse> {

}
