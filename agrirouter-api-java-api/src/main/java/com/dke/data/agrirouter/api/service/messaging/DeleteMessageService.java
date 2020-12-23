package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.messaging.AsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;

public interface DeleteMessageService
    extends MessagingService<DeleteMessageParameters, AsyncMessageSendingResult> {}
