package com.dke.data.agrirouter.api.enums

import java.util.*

enum class Gateway(val key: String) {

    REST("3"),

    MQTT("2");

    companion object {
        fun of(key: String): Optional<Gateway>? {
            return Arrays.stream(Gateway.values()).filter { a -> a.key.contentEquals(key) }.findFirst()
        }
    }

}