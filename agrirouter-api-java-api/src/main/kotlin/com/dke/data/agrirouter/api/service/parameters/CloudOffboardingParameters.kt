package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class CloudOffboardingParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var onboardingResponse: OnboardingResponse

    lateinit var endpointIds: List<String>

    override fun businessValidation() {
        if (endpointIds.isEmpty()) {
            this.rise("endpointIds", "There have to be endpoint IDs to delete.")
        }
    }

}