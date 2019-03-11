package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotNull

class ListEndpointsMessageParameters : ParameterValidation {

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    @NotNull
    lateinit var direction: agrirouter.request.payload.account.Endpoints.ListEndpointsQuery.Direction

}