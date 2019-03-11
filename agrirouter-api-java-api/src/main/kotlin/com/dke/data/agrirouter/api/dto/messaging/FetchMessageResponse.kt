package com.dke.data.agrirouter.api.dto.messaging

import com.dke.data.agrirouter.api.dto.messaging.inner.MessageResponse
import java.util.*

class FetchMessageResponse {

    lateinit var sensorAlternateId:String

    lateinit var capabilityAlternateId:String

    lateinit var command: MessageResponse


    constructor(fetchMessageJSONResponse: FetchMessageJSONResponse){
        sensorAlternateId = fetchMessageJSONResponse.sensorAlternateId
        capabilityAlternateId = fetchMessageJSONResponse.capabilityAlternateId
        command = MessageResponse( Base64.getDecoder().decode(fetchMessageJSONResponse.command.message))
    }

    constructor(){}
}