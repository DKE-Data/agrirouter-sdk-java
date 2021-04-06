package com.dke.data.agrirouter.api.dto.registrationrequest.secured

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthorizationResponseToken {

    lateinit var account: String
    lateinit var regcode: String
    lateinit var expires: String

}
