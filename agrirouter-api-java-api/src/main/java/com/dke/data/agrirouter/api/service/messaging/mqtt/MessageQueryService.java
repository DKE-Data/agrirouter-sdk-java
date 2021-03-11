package com.dke.data.agrirouter.api.service.messaging.mqtt;

import agrirouter.feed.response.FeedResponse;
import com.dke.data.agrirouter.api.service.messaging.encoding.MessageDecoder;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;

public interface MessageQueryService
    extends MessagingService<MessageQueryParameters>,
        MessageDecoder<FeedResponse.MessageQueryResponse> {}
