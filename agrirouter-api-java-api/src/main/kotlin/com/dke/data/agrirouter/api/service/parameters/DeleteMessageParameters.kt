package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class DeleteMessageParameters : ParameterValidation {

    @NotNull
    lateinit var onboardingResponse: OnboardingResponse

    var messageIds: List<String>? = null

    var senderIds: List<String>? = null

    var sentFromInSeconds: Long? = null

    var sentToInSeconds: Long? = null

    var applicationMessageID: String = ""


    override fun validate() {
        super.validate()
        if (null == messageIds && null == senderIds && null == sentFromInSeconds && null == sentToInSeconds) {
            throw IllegalParameterDefinitionException("There has to be a filter criteria for the query.")
        }
    }

}