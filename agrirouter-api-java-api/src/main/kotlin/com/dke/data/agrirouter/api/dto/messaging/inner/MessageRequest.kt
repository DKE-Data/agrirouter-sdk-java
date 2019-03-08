package com.dke.data.agrirouter.api.dto.messaging.inner

import com.dke.data.agrirouter.api.dto.messaging.SendMessageRequest
import com.sap.iotservices.common.protobuf.gateway.MeasureProtos

sealed class MessageRequest

data class MessageRequestJSON(val sendMessageRequest: SendMessageRequest):MessageRequest()
data class MessageRequestProtobuf(val sendMeasureRequest: MeasureProtos.MeasureRequest):MessageRequest()
