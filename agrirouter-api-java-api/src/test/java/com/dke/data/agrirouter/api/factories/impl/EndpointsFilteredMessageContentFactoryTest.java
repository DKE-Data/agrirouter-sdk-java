package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.factories.impl.parameters.EndpointsFilteredMessageParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

class EndpointsFilteredMessageContentFactoryTest {

  @Test
  void givenValidEndpointsFilteredParametersMessageShouldNotFail() {
    EndpointsFilteredMessageParameters endpointsFilteredMessageParameters =
        new EndpointsFilteredMessageParameters();
    endpointsFilteredMessageParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND;
    endpointsFilteredMessageParameters.technicalMessageType =
        TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    ByteString message = this.getInstanceToTest().message(endpointsFilteredMessageParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyEndpointsFilteredParametersMessageShouldNotFail() {
    EndpointsFilteredMessageParameters endpointsFilteredMessageParameters =
        new EndpointsFilteredMessageParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsFilteredMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenEndpointsFilteredParametersWithNullValuesMessageShouldThrowException() {
    EndpointsFilteredMessageParameters endpointsFilteredMessageParameters =
        new EndpointsFilteredMessageParameters();
    endpointsFilteredMessageParameters.direction = null;
    endpointsFilteredMessageParameters.technicalMessageType = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsFilteredMessageParameters));
  }

  private EndpointsFilteredMessageContentFactory getInstanceToTest() {
    return new EndpointsFilteredMessageContentFactory();
  }
}
