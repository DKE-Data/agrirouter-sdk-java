package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class MessageConfirmationForAllPendingMessagesParameters : AbstractParameterBase(), ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

}