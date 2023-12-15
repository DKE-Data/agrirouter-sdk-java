package com.dke.data.agrirouter.api.dto.onboard

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OnboardingError {
    class Error {
        class SubError {
            var code: Int = 0
            lateinit var message: String
            lateinit var target: String
        }

        var code: Int = 0
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
            onboardingError.error.code = -1
            onboardingError.error.message = "Unknown error occurred, the original error message is: $error"
            onboardingError.error.target = "Unknown target"
            onboardingError.error.details = arrayOf()
            return onboardingError
        }
    }
}