package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.payload.endpoint.Capabilities;
import com.dke.data.agrirouter.api.factories.impl.parameters.CapabilitiesMessageParameters;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

class CapabilitiesMessageContentFactoryTest
    extends AbstractMessageContentFactoryTest<CapabilitiesMessageContentFactory> {

  @Test
  void givenValidCapabilitiesMessageParameters_Message_ShouldNotFail() {
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
  void givenEmptyCapabilitiesMessageParameters_Message_ShouldThrowException() {
    CapabilitiesMessageParameters capabilitiesMessageParameters =
        new CapabilitiesMessageParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(capabilitiesMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenCapabilitiesMessageParametersWithNullValues_Message_ShouldNotFail() {
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

  @Override
  protected CapabilitiesMessageContentFactory getInstanceToTest() {
    return new CapabilitiesMessageContentFactory();
  }
}
