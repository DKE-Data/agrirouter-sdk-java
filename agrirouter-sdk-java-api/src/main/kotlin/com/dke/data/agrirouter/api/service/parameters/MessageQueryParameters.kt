package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class MessageQueryParameters : AbstractParameterBase(), ParameterValidation {

    var onboardingResponse: OnboardingResponse? = null

    var messageIds: List<String>? = null

    var senderIds: List<String>? = null

    var sentFromInSeconds: Long? = null

    var sentToInSeconds: Long? = null

    override fun technicalValidation() {
        nullCheck("onboardingResponse", onboardingResponse)
    }

    override fun businessValidation() {
        if (null == messageIds && null == senderIds && null == sentFromInSeconds && null == sentToInSeconds) {
            rise("There has to be a filter criteria for the query.", "messageIds", "senderIds", "sentFromInSeconds & sendToInSeconds", "")
        }
    }

}