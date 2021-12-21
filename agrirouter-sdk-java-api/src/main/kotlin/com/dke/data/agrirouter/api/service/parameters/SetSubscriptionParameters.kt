package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class SetSubscriptionParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    class Subscription : ParameterValidation {

        var technicalMessageType: TechnicalMessageType? = null

        var ddis: List<Int> = ArrayList()

        var position: Boolean = false

        override fun technicalValidation() {
            nullCheck("technicalMessageType",technicalMessageType)
        }
    }

    var subscriptions: List<Subscription> = ArrayList()

    override fun technicalValidation() {
        nullCheck("onboardingResponse",onboardingResponse)
    }

}