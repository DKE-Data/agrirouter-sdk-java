package com.dke.data.agrirouter.api.enums

/**
 * Common interface for all technical message types.
 */
interface TechnicalMessageType {

    /**
     * The key of the technical message type.
     */
    fun getKey(): String

    /**
     * The type url (if present) of the technical message type.
     */
    fun getTypeUrl(): String

    /**
     * Indicates whether the technical message type needs base64 encoding or not.
     */
    fun needsBase64EncodingAndHasToBeChunkedIfNecessary(): Boolean

}
