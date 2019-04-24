package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.container.DynamicAttributesContainer
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
open class FetchMessageParameters : DynamicAttributesContainer(), ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

}