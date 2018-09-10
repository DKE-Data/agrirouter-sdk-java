@file:JvmName("TimestampUtil")

package com.dke.data.agrirouter.api.util

import com.google.protobuf.Timestamp
import java.time.Instant

class TimestampUtil {

    /**
     * Will return a timestamp representation for the messaging.
     * @return -
     */
    fun current(): Timestamp {
        val timestampBuilder = Timestamp.newBuilder()
        timestampBuilder.seconds = Instant.now().epochSecond
        timestampBuilder.nanos = 1000000
        return timestampBuilder.build()
    }

    fun seconds(seconds: Long): Timestamp {
        if (seconds < 0) {
            throw IllegalArgumentException("Seconds [$seconds] should not be below 0.")
        }
        val timestampBuilder = Timestamp.newBuilder()
        timestampBuilder.seconds = seconds
        timestampBuilder.nanos = 1000000
        return timestampBuilder.build()
    }
}
