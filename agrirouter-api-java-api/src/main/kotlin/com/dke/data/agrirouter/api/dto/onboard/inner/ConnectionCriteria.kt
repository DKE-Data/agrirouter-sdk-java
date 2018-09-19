package com.dke.data.agrirouter.api.dto.onboard.inner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ConnectionCriteria {

    lateinit var gatewayId: String

    lateinit var measures: String

    lateinit var commands: String

}