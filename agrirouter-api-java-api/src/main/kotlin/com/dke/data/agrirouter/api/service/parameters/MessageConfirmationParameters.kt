package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class MessageConfirmationParameters : ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    @NotNull
    @NotBlank
    lateinit var applicationId: String

    @NotNull
    @NotBlank
    lateinit var certificationVersionId: String

    @NotNull
    lateinit var messageIds: List<String>

}