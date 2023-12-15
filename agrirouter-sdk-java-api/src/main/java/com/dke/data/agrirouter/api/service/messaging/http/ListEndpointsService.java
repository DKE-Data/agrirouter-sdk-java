package com.dke.data.agrirouter.api.service.messaging.http;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.messaging.HttpAsyncMessageSendingResult;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;

@SuppressWarnings("unused")
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
    HttpAsyncMessageSendingResult sendMessageToListAllWithExistingRouteAsync(
            OnboardingResponse onboardingResponse);

    /**
     * List all endpoints for the account, even those that do not have a route.
     *
     * @param onboardingResponse The onboard response for the endpoint.
     * @return The message ID.
     */
    HttpAsyncMessageSendingResult sendMessageToListAllAsync(OnboardingResponse onboardingResponse);
}
