package com.dke.data.agrirouter.api.service.parameters

import agrirouter.commons.Chunk
import agrirouter.request.Request
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import java.util.*
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class MessageHeaderParameters :  ParameterValidation {

    @NotNull
    lateinit var applicationMessageId: String

    var applicationMessageSeqNo: Long = 1

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    var teamSetContextId: String? = null

    @NotNull
    lateinit var mode: Request.RequestEnvelope.Mode

    var recipients: List<String> = Collections.emptyList()

    var chunkInfo: Chunk.ChunkComponent? = null

}