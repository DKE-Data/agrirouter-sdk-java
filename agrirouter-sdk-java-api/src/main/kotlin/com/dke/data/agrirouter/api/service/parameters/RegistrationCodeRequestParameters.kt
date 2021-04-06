package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class RegistrationCodeRequestParameters : AbstractParameterBase(), ParameterValidation {

    lateinit var applicationId: String

    override fun technicalValidation() {
        isBlank("applicationId",applicationId)
    }



}
