package com.dke.data.agrirouter.api.dto.registrationrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RegistrationCodeResponse {

    lateinit var registrationCode: String

    lateinit var validTo: String
    
}