package com.dke.data.agrirouter.api.dto.messaging

import com.dke.data.agrirouter.api.dto.messaging.inner.Message
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class SendMessageRequest {

    lateinit var sensorAlternateId: String

    lateinit var capabilityAlternateId: String

    @SerializedName("measures")
    @JsonProperty("measures")
    lateinit var messages: List<Message>

}

