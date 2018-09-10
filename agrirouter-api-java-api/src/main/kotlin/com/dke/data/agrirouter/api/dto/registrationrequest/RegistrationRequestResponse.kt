package com.dke.data.agrirouter.api.dto.registrationrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RegistrationRequestResponse {

    lateinit var registrationCode: String

    lateinit var validTo: String
    
}