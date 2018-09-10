package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotNull

open class FetchMessageParameters : ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

}