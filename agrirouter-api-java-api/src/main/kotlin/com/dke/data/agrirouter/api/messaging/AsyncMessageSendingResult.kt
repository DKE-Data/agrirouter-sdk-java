package com.dke.data.agrirouter.api.messaging

import java.util.concurrent.CompletableFuture

/**
 * Marker interface.
 */
interface AsyncMessageSendingResult<T> {

    fun accessResult(): CompletableFuture<T>

}
