package com.dke.data.agrirouter.api.dto.onboard

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OnboardingError {
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

    lateinit var error:Error;
}