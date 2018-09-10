package com.dke.data.agrirouter.api.dto.registrationrequest.secured

import com.dke.data.agrirouter.api.dto.onboard.inner.Authentication
import com.dke.data.agrirouter.api.dto.onboard.inner.ConnectionCriteria
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RegistrationRequestResponse {

    lateinit var signature : String
    lateinit var state: String
    lateinit var token: String
    var error: String? = null

}
