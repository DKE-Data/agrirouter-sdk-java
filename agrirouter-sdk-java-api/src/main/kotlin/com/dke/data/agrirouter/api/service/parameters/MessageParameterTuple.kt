package com.dke.data.agrirouter.api.service.parameters

/**
 * Containing a tuple for message sending, i.e. used after chunking the messages.
 */
class MessageParameterTuple(var messageHeaderParameters: MessageHeaderParameters, var payloadParameters: PayloadParameters) {
}