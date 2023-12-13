package com.dke.data.agrirouter.api.enums

import java.util.*

enum class SecuredOnboardingResponseType(val key: String) {

    VERIFY("verify"),

    ONBOARD("onboard");

    companion object {
        fun of(key: String): Optional<SecuredOnboardingResponseType>? {
            return Arrays.stream(entries.toTypedArray()).filter { a -> a.key.contentEquals(key) }
                .findFirst()
        }
    }

}