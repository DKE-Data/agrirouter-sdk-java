package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

open class XsuaaParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var clientId: String

    @NotNull
    @NotEmpty
    lateinit var clientSecret: String

}