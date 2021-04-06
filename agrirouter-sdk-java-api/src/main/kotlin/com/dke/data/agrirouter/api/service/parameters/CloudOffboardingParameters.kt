package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class CloudOffboardingParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    var endpointIds: List<String>? = null

    override fun technicalValidation() {
        nullCheck(onboardingResponse)
        nullCheck(endpointIds)
    }

    override fun businessValidation() {
        nullOrEmpty(endpointIds)
    }

}