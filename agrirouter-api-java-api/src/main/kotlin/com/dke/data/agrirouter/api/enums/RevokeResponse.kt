package com.dke.data.agrirouter.api.enums

enum class RevokeResponse(val key: Int) {
    SUCCESS(204),
    VALIDATION_ERROR(400),
    UNAUTHORIZED(401)
}