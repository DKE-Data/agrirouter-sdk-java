package com.dke.data.agrirouter.api.service.messaging;

import com.dke.data.agrirouter.api.messaging.AsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.SetCapabilitiesParameters;

/**
 * Service interface set capabilities.
 */
public interface SetCapabilityService extends MessagingService<SetCapabilitiesParameters, AsyncMessageSendingResult> {}
