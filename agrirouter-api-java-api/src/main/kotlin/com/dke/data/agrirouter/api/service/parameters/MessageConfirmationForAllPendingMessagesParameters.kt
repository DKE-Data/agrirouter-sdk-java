package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class MessageConfirmationForAllPendingMessagesParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var onboardingResponse: OnboardingResponse

    override fun technicalValidation() {
        nullCheck(onboardingResponse)
    }

}