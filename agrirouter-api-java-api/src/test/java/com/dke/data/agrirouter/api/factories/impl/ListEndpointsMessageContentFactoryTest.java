package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agrirouter.request.payload.account.Endpoints;
import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.service.parameters.ListEndpointsParameters;
import com.google.protobuf.ByteString;
import kotlin.UninitializedPropertyAccessException;
import org.junit.jupiter.api.Test;

class ListEndpointsMessageContentFactoryTest {

  @Test
  void givenValidEndpointsUnfilteredMessageParametersMessageShouldNotFail() {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = Endpoints.ListEndpointsQuery.Direction.SEND;
    listEndpointsParameters.technicalMessageType = TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    ByteString message = this.getInstanceToTest().message(listEndpointsParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptyEndpointsUnfilteredMessageParametersMessageShouldThrowException() {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(listEndpointsParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenEndpointsUnfilteredMessageParametersWithNullValuesMessageShouldThrowException() {
    ListEndpointsParameters listEndpointsParameters = new ListEndpointsParameters();
    listEndpointsParameters.direction = null;
    listEndpointsParameters.technicalMessageType = null;
    assertThrows(
        UninitializedPropertyAccessException.class,
        () -> this.getInstanceToTest().message(listEndpointsParameters));
  }

  private ListEndpointsMessageContentFactory getInstanceToTest() {
    return new ListEndpointsMessageContentFactory();
  }
}
