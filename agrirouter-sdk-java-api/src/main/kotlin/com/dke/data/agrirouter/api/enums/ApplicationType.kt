package com.dke.data.agrirouter.api.enums

import java.util.*

enum class ApplicationType(val key: String) {

    APPLICATION("application"),

    CLOUD("cloud");

    companion object {
        fun of(key: String): Optional<ApplicationType>? {
            return Arrays.stream(values()).filter { a -> a.key.contentEquals(key) }.findFirst()
        }
    }

}