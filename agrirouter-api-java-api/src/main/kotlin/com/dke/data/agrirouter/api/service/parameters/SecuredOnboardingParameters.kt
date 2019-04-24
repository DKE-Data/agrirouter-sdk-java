package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.CertificationType
import com.dke.data.agrirouter.api.service.ParameterValidation
import lombok.ToString
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Parameters class. Encapsulation for the services.
 */
@ToString
class SecuredOnboardingParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

    @NotNull
    @NotEmpty
    lateinit var privateKey: String

    @NotNull
    @NotEmpty
    lateinit var publicKey: String

    @NotNull
    @NotEmpty
    lateinit var uuid: String

    @NotNull
    @NotEmpty
    lateinit var certificationVersionId: String

    @NotNull
    @NotEmpty
    lateinit var gatewayId: String

    @NotNull
    lateinit var certificationType: CertificationType


    @NotNull
    @NotBlank
    lateinit var registrationCode: String

    var applicationMessageID: String = ""

}