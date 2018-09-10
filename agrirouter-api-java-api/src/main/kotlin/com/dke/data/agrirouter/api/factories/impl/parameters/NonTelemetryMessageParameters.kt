package com.dke.data.agrirouter.api.factories.impl.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class NonTelemetryMessageParameters : ParameterValidation {

    @NotNull
    @NotBlank
    lateinit var content: String

}