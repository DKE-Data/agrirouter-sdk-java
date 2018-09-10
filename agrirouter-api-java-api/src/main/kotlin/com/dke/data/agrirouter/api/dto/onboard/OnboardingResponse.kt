package com.dke.data.agrirouter.api.dto.onboard

import com.dke.data.agrirouter.api.dto.onboard.inner.Authentication
import com.dke.data.agrirouter.api.dto.onboard.inner.ConnectionCriteria
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OnboardingResponse {

    lateinit var deviceAlternateId: String

    lateinit var capabilityAlternateId: String

    lateinit var sensorAlternateId: String

    lateinit var connectionCriteria: ConnectionCriteria

    lateinit var authentication: Authentication

}
