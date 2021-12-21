package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class ListEndpointsParameters : AbstractParameterBase(), ParameterValidation {


    var onboardingResponse: OnboardingResponse? = null

    var technicalMessageType: TechnicalMessageType? = null

    var direction: agrirouter.request.payload.account.Endpoints.ListEndpointsQuery.Direction? = null

    var unfilteredList: Boolean = false

    override fun technicalValidation() {
        nullCheck("onboardingResponse",onboardingResponse)
        nullCheck("technicalMessageType",technicalMessageType)
        nullCheck("direction",direction)
    }

}