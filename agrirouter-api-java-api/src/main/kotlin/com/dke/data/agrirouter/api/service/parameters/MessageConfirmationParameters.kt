package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class MessageConfirmationParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var onboardingResponse: OnboardingResponse

    lateinit var messageIds: List<String>

    override fun technicalValidation() {
        nullCheck(onboardingResponse)
        nullCheck(messageIds)
    }

    override fun businessValidation() {
        nullOrEmpty(messageIds)
    }

}