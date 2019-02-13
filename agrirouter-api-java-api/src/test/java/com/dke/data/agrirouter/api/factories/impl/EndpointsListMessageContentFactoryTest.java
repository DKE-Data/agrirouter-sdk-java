package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.parameters.EndpointsListParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

class EndpointsListMessageContentFactoryTest {

  @Test
  void givenValidEndpointsUnfilteredMessageParametersMessageShouldNotFail() {
    EndpointsListParameters endpointsListParameters = new EndpointsListParameters();
    endpointsListParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND;
    endpointsListParameters.technicalMessageType = TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    ByteString message = this.getInstanceToTest().message(endpointsListParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyEndpointsUnfilteredMessageParametersMessageShouldThrowException() {
    EndpointsListParameters endpointsListParameters = new EndpointsListParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsListParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenEndpointsUnfilteredMessageParametersWithNullValuesMessageShouldThrowException() {
    EndpointsListParameters endpointsListParameters = new EndpointsListParameters();
    endpointsListParameters.direction = null;
    endpointsListParameters.technicalMessageType = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(endpointsListParameters));
  }

  private EndpointsListMessageContentFactory getInstanceToTest() {
    return new EndpointsListMessageContentFactory();
  }
}
