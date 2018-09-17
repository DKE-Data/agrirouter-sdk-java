package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class EndpointsUnfilteredMessageParameters : ParameterValidation {

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    @NotNull
    lateinit var direction: agrirouter.request.payload.account.Endpoints.ListEndpointsQuery.Direction

}