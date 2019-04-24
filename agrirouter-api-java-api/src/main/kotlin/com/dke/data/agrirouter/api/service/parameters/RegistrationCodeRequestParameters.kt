package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class RegistrationCodeRequestParameters : AbstractParameterBase(), ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

}
