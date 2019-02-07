package com.dke.data.agrirouter.api.factories.impl;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.parameters.EndpointsUnfilteredMessageParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointsUnfilteredMessageContentFactoryTest {

  @Test
  void givenValidEndpointsUnfilteredMessageParametersMessageShouldNotFail() {
    EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters =
        new EndpointsUnfilteredMessageParameters();
    endpointsUnfilteredMessageParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND;
    endpointsUnfilteredMessageParameters.technicalMessageType =
        TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    ByteString message = this.getInstanceToTest().message(endpointsUnfilteredMessageParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyEndpointsUnfilteredMessageParametersMessageShouldThrowException() {
    EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters =
        new EndpointsUnfilteredMessageParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsUnfilteredMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenEndpointsUnfilteredMessageParametersWithNullValuesMessageShouldThrowException() {
    EndpointsUnfilteredMessageParameters endpointsUnfilteredMessageParameters =
        new EndpointsUnfilteredMessageParameters();
    endpointsUnfilteredMessageParameters.direction = null;
    endpointsUnfilteredMessageParameters.technicalMessageType = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsUnfilteredMessageParameters));
  }

  private EndpointsUnfilteredMessageContentFactory getInstanceToTest() {
    return new EndpointsUnfilteredMessageContentFactory();
  }
}
