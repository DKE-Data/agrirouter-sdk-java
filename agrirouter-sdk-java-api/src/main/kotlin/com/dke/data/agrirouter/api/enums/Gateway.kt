package com.dke.data.agrirouter.api.enums

import java.util.*

@Suppress("unused")
enum class Gateway(val key: String) {

    REST("3"),

    MQTT("2");

    companion object {
        fun of(key: String): Optional<Gateway>? {
            return Arrays.stream(entries.toTypedArray()).filter { a -> a.key.contentEquals(key) }.findFirst()
        }
    }

}