package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class CloudOnboardingParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    var endpointDetails: List<EndpointDetailsParameters>? = null

    override fun technicalValidation() {
        nullCheck("onboardingResponse",onboardingResponse)
        nullCheck("endpointDetails",endpointDetails)
    }

    override fun businessValidation() {
        nullOrEmpty("endpointDetails",endpointDetails)
        endpointDetails?.forEach {
            it.validate()
        }
    }

    class EndpointDetailsParameters : ParameterValidation {

        var endpointId: String? = null

        var endpointName: String? = null

        override fun technicalValidation() {
            isBlank("endpointId",endpointId)
            isBlank("endpointName",endpointName)
        }

    }

}