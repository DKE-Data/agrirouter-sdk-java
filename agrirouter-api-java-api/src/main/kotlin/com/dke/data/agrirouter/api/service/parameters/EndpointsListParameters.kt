package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class EndpointsListParameters : ParameterValidation {


    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    @NotNull
    lateinit var direction: agrirouter.request.payload.account.Endpoints.ListEndpointsQuery.Direction

    var unFilteredList: Boolean = false
}