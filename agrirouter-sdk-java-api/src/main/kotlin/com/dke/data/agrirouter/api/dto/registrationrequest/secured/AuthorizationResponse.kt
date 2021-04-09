package com.dke.data.agrirouter.api.dto.registrationrequest.secured

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthorizationResponse {

    lateinit var signature : String
    lateinit var state: String
    lateinit var token: String
    var error: String? = null

    /**
     * Returns true, if an error accoured while generating token serverside
     */
    fun hasError():Boolean{
        return !error.isNullOrBlank()
    }
}
