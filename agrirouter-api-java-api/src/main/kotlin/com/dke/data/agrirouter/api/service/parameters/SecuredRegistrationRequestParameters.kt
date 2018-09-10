package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString

@ToString
class SecuredRegistrationRequestParameters : ParameterValidation {

    lateinit var applicationId : String

    lateinit var redirectUri: String

}