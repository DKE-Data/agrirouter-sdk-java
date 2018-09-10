package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class RegistrationRequestParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

}
