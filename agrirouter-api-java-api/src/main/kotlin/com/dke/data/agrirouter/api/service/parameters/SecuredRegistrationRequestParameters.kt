package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation

class SecuredRegistrationRequestParameters : ParameterValidation {

    lateinit var applicationId : String

    lateinit var redirectUri: String

}