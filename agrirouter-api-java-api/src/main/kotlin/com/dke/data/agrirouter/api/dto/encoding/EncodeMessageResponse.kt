package com.dke.data.agrirouter.api.dto.encoding

import com.sap.iotservices.common.protobuf.gateway.MeasureRequestMessageProtos

/**
 * This class holds the data to be returned from an message encoding function
 *
 * @param applicationMessageID the generated application message ID
 * @param encodedMessage the encoded message
 *
 */
sealed class EncodeMessageResponse(val applicationMessageID: String){
    data class EncodeMessageResponseJSON(val applicationMessageID_Parent: String,
                                         val encodedMessageBase64 : String):
            EncodeMessageResponse(applicationMessageID_Parent)
    data class EncodeMessageResponseProtobuf(val applicationMessageID_Parent: String,
                                             val encodedMessageProtobuf : MeasureRequestMessageProtos.MeasureRequestMessage):
            EncodeMessageResponse(applicationMessageID_Parent)
}
