package com.dke.data.agrirouter.api.service.messaging.mqtt;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.MqttAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;

public interface ListEndpointsService extends MessagingService<ListEndpointsParameters> {

    /**
     * List all endpoints with a route to the dedicated endpoint.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    String sendMessageToListAllWithExistingRoute(OnboardingResponse onboardingResponse);

    /**
     * List all endpoints for the account, even those that do not have a route.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    String sendMessageToListAll(OnboardingResponse onboardingResponse);

    /**
     * List all endpoints with a route to the dedicated endpoint.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    MqttAsyncMessageSendingResult sendMessageToListAllWithExistingRouteAsync(
            OnboardingResponse onboardingResponse);

    /**
     * List all endpoints for the account, even those that do not have a route.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    MqttAsyncMessageSendingResult sendMessageToListAllAsync(OnboardingResponse onboardingResponse);
}
