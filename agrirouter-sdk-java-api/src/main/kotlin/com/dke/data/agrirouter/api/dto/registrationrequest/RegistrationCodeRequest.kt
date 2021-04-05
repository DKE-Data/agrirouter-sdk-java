package com.dke.data.agrirouter.api.dto.registrationrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RegistrationCodeRequest {

    lateinit var accountId: String

    lateinit var applicationId: String

}