package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class MessageConfirmationParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    var messageIds: List<String>? = null

    override fun technicalValidation() {
        nullCheck("onboardingResponse",onboardingResponse)
        nullCheck("messageIds",messageIds)
    }

}