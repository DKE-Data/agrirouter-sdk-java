package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class CloudOnboardingParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var onboardingResponse: OnboardingResponse

    lateinit var endpointDetails: List<EndpointDetailsParameters>

    override fun validate() {
        super.validate()
        if (endpointDetails.isEmpty()) {
            throw IllegalParameterDefinitionException("There have to be endpoint details to onboard.")
        }
    }

    class EndpointDetailsParameters {

        @org.jetbrains.annotations.NotNull
        lateinit var endpointId: String

        @org.jetbrains.annotations.NotNull
        lateinit var endpointName: String

    }

}