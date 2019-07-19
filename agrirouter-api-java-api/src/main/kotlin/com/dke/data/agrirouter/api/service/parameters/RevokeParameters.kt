package com.dke.data.agrirouter.api.service.parameters

import com.dke.data.agrirouter.api.enums.ApplicationType
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
class RevokeParameters : ParameterValidation {

    @NotNull
    @NotEmpty
    lateinit var applicationId: String

    @NotNull
    @NotEmpty
    lateinit var accountId: String

    @NotNull
    @NotEmpty
    lateinit var endpointIds: List<String>

    @NotNull
    @NotEmpty
    lateinit var privateKey: String

    @NotNull
    @NotEmpty
    lateinit var publicKey: String
}