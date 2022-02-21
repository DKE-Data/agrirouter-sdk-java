package com.dke.data.agrirouter.impl.messaging.helper;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import com.dke.data.agrirouter.api.service.parameters.MessageQueryParameters;
import com.dke.data.agrirouter.impl.common.UtcTimeService;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

/** Interface to abstract the message query creation. */
public interface QueryAllMessagesParameterCreator {

  /**
   * Create message parameters to query all messages.
   *
   * @param onboardingResponse -
   * @return -
   */
  @NotNull
  default MessageQueryParameters createMessageParametersToQueryAll(
      OnboardingResponse onboardingResponse) {
    MessageQueryParameters messageQueryParameters = new MessageQueryParameters();
    messageQueryParameters.setOnboardingResponse(onboardingResponse);
    messageQueryParameters.setMessageIds(Collections.emptyList());
    messageQueryParameters.setSenderIds(Collections.emptyList());
    messageQueryParameters.setSentFromInSeconds(
        UtcTimeService.inThePast(UtcTimeService.FOUR_WEEKS_AGO).toEpochSecond());
    messageQueryParameters.setSentToInSeconds(UtcTimeService.now().toEpochSecond());
    return messageQueryParameters;
  }
}
