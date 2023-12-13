package com.dke.data.agrirouter.api.messaging

import java.util.concurrent.CompletableFuture

@Suppress("unused")
class HttpAsyncMessageSendingResult(
    val response: CompletableFuture<MessageSendingResponse>,
    val messageId: String
) : AsyncMessageSendingResult<MessageSendingResponse> {
    override fun accessResult(): CompletableFuture<MessageSendingResponse> {
        return response
    }

}