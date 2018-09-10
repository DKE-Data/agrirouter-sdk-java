package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.SecuredOnboardingResponseType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString

@ToString
class AuthenticationUrlParameters : ParameterValidation {

    lateinit var applicationId : String

    lateinit var responseType: SecuredOnboardingResponseType

    lateinit var state: String

    lateinit var redirectUri: String

}