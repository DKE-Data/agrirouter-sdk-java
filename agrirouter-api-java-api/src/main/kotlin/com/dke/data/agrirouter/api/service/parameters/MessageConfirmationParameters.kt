package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class MessageConfirmationParameters : ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse
    @NotNull
    lateinit var messageIds: List<String>

}