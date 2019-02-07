package com.dke.data.agrirouter.api.factories.impl;

import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.factories.impl.parameters.CapabilitiesMessageParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapabilitiesMessageContentFactoryTest {

  @Test
  void givenValidCapabilitiesMessageParametersMessageShouldNotFail() {
    List<Capabilities.CapabilitySpecification.Capability> capabilities = new ArrayList<>();
    CapabilitiesMessageParameters capabilitiesMessageParameters =
        new CapabilitiesMessageParameters();
    capabilitiesMessageParameters.setCapabilities(capabilities);
    capabilitiesMessageParameters.setAppCertificationId("1");
    capabilitiesMessageParameters.setAppCertificationVersionId("1");
    ByteString message = this.getInstanceToTest().message(capabilitiesMessageParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyCapabilitiesMessageParametersMessageShouldThrowException() {
    CapabilitiesMessageParameters capabilitiesMessageParameters =
        new CapabilitiesMessageParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(capabilitiesMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenCapabilitiesMessageParametersWithNullValuesMessageShouldNotFail() {
    List<Capabilities.CapabilitySpecification.Capability> capabilities = new ArrayList<>();
    CapabilitiesMessageParameters capabilitiesMessageParameters =
        new CapabilitiesMessageParameters();
    capabilitiesMessageParameters.setCapabilities(capabilities);
    capabilitiesMessageParameters.appCertificationId = null;
    capabilitiesMessageParameters.appCertificationVersionId = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(capabilitiesMessageParameters));
  }

  private CapabilitiesMessageContentFactory getInstanceToTest() {
    return new CapabilitiesMessageContentFactory();
  }
}
