package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.messaging.AsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.CloudOnboardingParameters;

public interface CloudOnboardingService
    extends MessagingService<CloudOnboardingParameters, AsyncMessageSendingResult> {}
