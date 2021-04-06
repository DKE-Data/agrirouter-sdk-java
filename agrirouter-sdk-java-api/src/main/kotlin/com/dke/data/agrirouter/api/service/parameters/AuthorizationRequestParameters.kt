package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase

/**
 * Parameters class. Encapsulation for the services.
 */
class AuthorizationRequestParameters : AbstractParameterBase(), ParameterValidation {

    var applicationId : String = ""

    var redirectUri: String = ""

    var state: String = ""

    var responseType: SecuredOnboardingResponseType = SecuredOnboardingResponseType.ONBOARD

    override fun technicalValidation() {
        isBlank("applicationId",applicationId)
    }

}