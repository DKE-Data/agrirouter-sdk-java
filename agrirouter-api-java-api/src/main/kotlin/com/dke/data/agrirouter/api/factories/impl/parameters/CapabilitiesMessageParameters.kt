package com.dke.data.agrirouter.api.factories.impl.parameters

import agrirouter.request.payload.endpoint.Capabilities
import com.dke.data.agrirouter.api.service.ParameterValidation
import org.jetbrains.annotations.NotNull

class CapabilitiesMessageParameters : ParameterValidation {

    @NotNull
    lateinit var capabilities: List<Capabilities.CapabilitySpecification.Capability>

    @NotNull
    lateinit var appCertificationId: String

    @NotNull
    lateinit var appCertificationVersionId: String

    var enablePushNotifications: Capabilities.CapabilitySpecification.PushNotification = Capabilities.CapabilitySpecification.PushNotification.DISABLED
}