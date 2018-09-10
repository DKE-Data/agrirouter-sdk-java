package com.dke.data.agrirouter.api.dto.onboard.inner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Authentication {

    lateinit var type: String

    lateinit var secret: String

    lateinit var certificate: String

}
