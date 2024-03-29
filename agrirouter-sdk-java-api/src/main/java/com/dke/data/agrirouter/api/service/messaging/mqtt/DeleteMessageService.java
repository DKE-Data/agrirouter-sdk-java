package com.dke.data.agrirouter.api.service.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;

@SuppressWarnings("unused")
public interface DeleteMessageService extends MessagingService<DeleteMessageParameters> {

    /**
     * Delete all messages in the feed. The function will delete all messages based on the time
     * interval of 4 weeks (which is the maximum of time the messages are stored within the
     * agrirouter).
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    String sendMessageToDeleteAll(OnboardingResponse onboardingResponse);

    /**
     * Delete all messages in the feed. The function will delete all messages based on the time
     * interval of 4 weeks (which is the maximum of time the messages are stored within the
     * agrirouter).
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    MqttAsyncMessageSendingResult sendMessageToDeleteAllAsync(OnboardingResponse onboardingResponse);
}
