package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class RegistrationCodeRequestParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

}
