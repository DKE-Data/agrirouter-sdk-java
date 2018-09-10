package com.dke.data.agrirouter.api.dto.messaging

import com.dke.data.agrirouter.api.dto.messaging.inner.Message
import com.fasterxml.jackson.annotation.JsonProperty

class SendMessageRequest {

    lateinit var sensorAlternateId: String

    lateinit var capabilityAlternateId: String

    @JsonProperty("measures")
    lateinit var messages: List<Message>

}

