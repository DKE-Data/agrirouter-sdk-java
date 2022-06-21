package com.dke.data.agrirouter.api.dto.revoke

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RevokingError {
    class Error{
        class SubError {
            var code: Int = 0
            lateinit var message: String
            lateinit var target: String
        }
        var code:Int = 0
        lateinit var message:String
        lateinit var target:String
        lateinit var details:Array<SubError>
    }

    lateinit var error:Error

    companion object {
        @JvmStatic
        fun unknownError(error: String): RevokingError {
            val revokingError = RevokingError()
            revokingError.error = Error()
            revokingError.error.code = -1
            revokingError.error.message = "Unknown error occurred, the original error message is: $error"
            revokingError.error.target = "Unknown target"
            revokingError.error.details = arrayOf()
            return revokingError
        }
    }
}