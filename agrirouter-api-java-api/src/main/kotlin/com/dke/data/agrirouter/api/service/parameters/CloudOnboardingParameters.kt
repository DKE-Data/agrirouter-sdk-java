package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class CloudOnboardingParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var onboardingResponse: OnboardingResponse

    lateinit var endpointDetails: List<EndpointDetailsParameters>

    override fun businessValidation() {
        if (endpointDetails.isEmpty()) {
            this.rise("endpointDetails", "There have to be endpoint IDs to delete.")
        }
    }

    class EndpointDetailsParameters {

        lateinit var endpointId: String

        lateinit var endpointName: String

    }

}