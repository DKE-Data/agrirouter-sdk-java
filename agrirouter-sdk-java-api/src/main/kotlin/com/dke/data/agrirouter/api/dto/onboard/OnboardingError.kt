package com.dke.data.agrirouter.api.dto.onboard

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OnboardingError {
    class Error {
        class SubError {
            var code: String = ""
            lateinit var message: String
            lateinit var target: String
        }

        var code: String = ""
        lateinit var message: String
        lateinit var target: String
        lateinit var details: Array<SubError>
    }

    lateinit var error: Error

    companion object {
        @JvmStatic
        fun unknownError(error: String): OnboardingError {
            val onboardingError = OnboardingError()
            onboardingError.error = Error()
            onboardingError.error.code = "UNKNOWN_ERROR"
            onboardingError.error.message = "Unknown error occurred, the original error message is: $error"
            onboardingError.error.target = "Unknown target"
            onboardingError.error.details = arrayOf()
            return onboardingError
        }
    }
}