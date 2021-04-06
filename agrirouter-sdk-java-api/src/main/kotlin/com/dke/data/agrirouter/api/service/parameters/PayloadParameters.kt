package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.service.ParameterValidation
import com.dke.data.agrirouter.api.service.parameters.base.AbstractParameterBase
import com.google.protobuf.ByteString

/**
 * Every endpoint can send message based on their capabilities. The size of a message is however limited. A message contains 2 parts: Header and Body. The limitation of a message is defined as follows:
 * - Body size is equivalent of 1024000 characters/signs
 * - Total message size is limited to 1468000 characters/signs
 * - Message that are above this limit will be rejected.
 * The AR will return an error indicated that the message size is above the limit.
 * If the message size is above 5 MB the AR will not return any error. In order to send message with size large that above threshold, the message must be split in chunks with the above limit.
 */
const val MAX_LENGTH_FOR_MESSAGES = 1024000

/**
 * Parameters class. Encapsulation for the services.
 */
class PayloadParameters : AbstractParameterBase(), ParameterValidation {

    var typeUrl: String = ""

    var value: ByteString? = null

    override fun technicalValidation() {
        nullCheck("typeUrl",typeUrl)
        nullCheck("value",value)
    }

    /**
     * Determining whether the message should be chunked.
     */
    fun shouldBeChunked(): Boolean {
        return value!!.toStringUtf8().length > MAX_LENGTH_FOR_MESSAGES
    }

    /**
     * The maximum length for messages / the payload.
     */
    fun maxLengthForMessages(): Int {
        return MAX_LENGTH_FOR_MESSAGES
    }

    fun copy(payload: PayloadParameters) {
        applicationMessageId = payload.applicationMessageId
        teamsetContextId = payload.teamsetContextId
        sequenceNumber = payload.sequenceNumber
        typeUrl = payload.typeUrl
        value = payload.value
    }

}