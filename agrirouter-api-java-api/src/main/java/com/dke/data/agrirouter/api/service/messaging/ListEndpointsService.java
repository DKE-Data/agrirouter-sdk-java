package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.messaging.AsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;

public interface ListEndpointsService
    extends MessagingService<ListEndpointsParameters, AsyncMessageSendingResult> {}
