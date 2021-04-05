package com.dke.data.agrirouter.api.dto.revoke

import com.dke.data.agrirouter.api.dto.onboard.OnboardingError
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

    lateinit var error:Error;
}