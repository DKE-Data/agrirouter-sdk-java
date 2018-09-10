package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.ApplicationType
import com.dke.data.agrirouter.api.enums.CertificationType
import com.dke.data.agrirouter.api.service.ParameterValidation
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class OnboardingParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var clientId: String

    @NotNull
    @NotEmpty
    lateinit var clientSecret: String

    @NotNull
    @NotEmpty
    lateinit var accountId: String

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

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
    lateinit var applicationType: ApplicationType

    @NotNull
    @NotBlank
    lateinit var registrationCode: String

}