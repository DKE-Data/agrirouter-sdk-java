package com.dke.data.agrirouter.api.dto.messaging

import com.dke.data.agrirouter.api.dto.messaging.inner.Message
import com.dke.data.agrirouter.api.dto.messaging.inner.MessageResponse

class FetchMessageResponse {

    lateinit var sensorAlternateId:String

    lateinit var capabilityAlternateId:String

    lateinit var command: MessageResponse
}