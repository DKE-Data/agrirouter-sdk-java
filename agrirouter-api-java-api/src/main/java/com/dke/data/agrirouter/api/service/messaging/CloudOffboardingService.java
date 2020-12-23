package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.messaging.AsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.CloudOffboardingParameters;

public interface CloudOffboardingService
    extends MessagingService<CloudOffboardingParameters, AsyncMessageSendingResult> {}
