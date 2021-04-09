package com.dke.data.agrirouter.api.dto.onboard

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OnboardingRequest {

    lateinit var id: String

    lateinit var applicationId: String

    lateinit var certificationVersionId: String

    lateinit var gatewayId: String

    lateinit var UTCTimestamp: String

    lateinit var timeZone: String

    lateinit var certificateType: String

}