package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class SecuredRegistrationRequestParameters : ParameterValidation {

    lateinit var applicationId : String

    var redirectUri: String = ""

    lateinit var state: String

    var responseType: SecuredOnboardingResponseType = SecuredOnboardingResponseType.ONBOARD
}