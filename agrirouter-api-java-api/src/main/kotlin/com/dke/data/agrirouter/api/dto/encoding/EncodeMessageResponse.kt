package com.dke.data.agrirouter.api.dto.encoding

import com.sap.iotservices.common.protobuf.gateway.MeasureRequestMessageProtos

/**
 * This class holds the data to be returned from an message encoding function
 *
 * @param applicationMessageID the generated application message ID
 * @param encodedMessage the encoded message
 *
 */
data class EncodeMessageResponse(
        val applicationMessageID: String,
        val encodedMessageBase64 : String? = null,
        val encodedMessageProtobuf : MeasureRequestMessageProtos.MeasureRequestMessage? = null
)