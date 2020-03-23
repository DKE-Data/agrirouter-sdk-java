package com.dke.data.agrirouter.api.service.parameters

import agrirouter.commons.Chunk
import agrirouter.commons.MessageOuterClass
import agrirouter.request.Request
import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.enums.TechnicalMessageType
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException
import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import lombok.ToString
import java.util.*
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class SendContentMessageParameters : AbstractParameterBase(), ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    var base64EncodedMessageContent: String? = null

    var typeUrl: String = TechnicalMessageType.EMPTY.key

    var chunkSize: Int = 0

    @NotNull
    lateinit var technicalMessageType: TechnicalMessageType

    @NotNull
    lateinit var mode: Request.RequestEnvelope.Mode

    var recipients: List<String> = Collections.emptyList()

    var metadata: MessageOuterClass.Metadata? = null

    override fun validate() {
        super.validate()
        if (null == base64EncodedMessageContent) {
            throw IllegalParameterDefinitionException("There has to be a message content for the message.")
        }
    }

}