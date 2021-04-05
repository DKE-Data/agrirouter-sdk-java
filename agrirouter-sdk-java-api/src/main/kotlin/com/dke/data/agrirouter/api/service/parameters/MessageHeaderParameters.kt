package com.dke.data.agrirouter.api.service.parameters

import agrirouter.commons.Chunk
import agrirouter.commons.MessageOuterClass
import agrirouter.request.Request
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import java.util.*

/**
 * Parameters class. Encapsulation for the services.
 */
class MessageHeaderParameters :  ParameterValidation {

    var applicationMessageId: String? = null

    var applicationMessageSeqNo: Long = 1

    var technicalMessageType: TechnicalMessageType? = null

    var teamSetContextId: String? = null

    var mode: Request.RequestEnvelope.Mode? = null

    var recipients: List<String> = Collections.emptyList()

    var chunkInfo: Chunk.ChunkComponent? = null

    var metadata: MessageOuterClass.Metadata? = null

    override fun technicalValidation() {
        isBlank(applicationMessageId)
        nullCheck(technicalMessageType)
        nullCheck(mode)
    }

}