package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class ListEndpointsParameters : AbstractParameterBase(), ParameterValidation {


    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    @NotNull
    lateinit var direction: agrirouter.request.payload.account.Endpoints.ListEndpointsQuery.Direction

    var unfilteredList: Boolean = false

}