package com.dke.data.agrirouter.api.messaging

import java.util.concurrent.CompletableFuture

class MqttAsyncMessageSendingResult(private val result: CompletableFuture<String>) : AsyncMessageSendingResult<String> {
    override fun accessResult(): CompletableFuture<String> {
        return result
    }

}