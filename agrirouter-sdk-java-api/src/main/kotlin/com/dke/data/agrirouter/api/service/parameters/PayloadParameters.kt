package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import com.google.protobuf.ByteString

/**
 * Every endpoint can send messages based on its capabilities. The size of a message is however limited. A message contains 2 parts: Header and Body. The limitation of a message is defined as follows:
 * - The maximum body size is equivalent of 1024000 characters/signs
 * - Since the chunking is performed on the raw message data this means, that we have to lower the MAX_LENGTH_FOR_MESSAGES to allow Base64 encoding afterwards.
 * - Total message size is limited to 1468000 characters/signs
 * - Messages that are above this limit will be rejected.
 * The AR will return an error indicating that the message size is above the limit.
 * If the message size is above 5 MB the AR will not return any error. In order to send messages with sizes above threshold, these messages must be split into chunks with the above limit.
 */
const val MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT = 767997

/**
 * Parameters class. Encapsulation for the services.
 */
class PayloadParameters : AbstractParameterBase(), ParameterValidation {

    var typeUrl: String = ""

    var value: ByteString? = null

    override fun technicalValidation() {
        nullCheck("typeUrl", typeUrl)
        nullCheck("value", value)
    }

    /**
     * Determining whether the message should be chunked.
     */
    fun shouldBeChunked(): Boolean {
        return value!!.toStringUtf8().length > MAX_LENGTH_FOR_RAW_MESSAGE_CONTENT
    }

    fun copyFrom(payload: PayloadParameters) {
        applicationMessageId = payload.applicationMessageId
        teamsetContextId = payload.teamsetContextId
        sequenceNumber = payload.sequenceNumber
        typeUrl = payload.typeUrl
        value = payload.value
    }

}