package com.dke.data.agrirouter.impl.messaging.helper;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.DeleteMessageParameters;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Interface to avaoid duplicate parameter creation.
 */
public interface DeleteAllMessagesParameterCreator {

    /**
     * Create message parameters to delete all messages.
     *
     * @param onboardingResponse -
     * @return -
     */
    @NotNull
    default DeleteMessageParameters createMessageParametersToDeleteAllMessages(
            OnboardingResponse onboardingResponse) {
        final DeleteMessageParameters deleteMessageParameters = new DeleteMessageParameters();
        deleteMessageParameters.setOnboardingResponse(onboardingResponse);
        deleteMessageParameters.setMessageIds(Collections.emptyList());
        deleteMessageParameters.setSenderIds(Collections.emptyList());
        deleteMessageParameters.setSentFromInSeconds(
                UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
        deleteMessageParameters.setSentToInSeconds(UtcTimeService.now().toEpochSecond());
        return deleteMessageParameters;
    }
}
