package com.dke.data.agrirouter.api.factories

import com.google.protobuf.ByteString

/**
 * Basic API for message content factories to ensure a similar pattern for all classes.
 */
interface MessageContentFactory<P> {

    /**
     * Generate the message content using the given paramters.
     *
     * @param parameters Parameters needed for the creation of the message.
     * @return Encoded protobuf representation of the message.
     */
    fun message(vararg parameters: P): ByteString

}