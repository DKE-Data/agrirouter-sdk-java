package com.dke.data.agrirouter.api.factories.impl;

import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubscriptionMessageContentFactoryTest {

  @Test
  void givenValidSubscriptionMessageParametersMessageShouldNotFail() {
    SubscriptionMessageParameters subscriptionMessageParameters =
        new SubscriptionMessageParameters();
    List<Integer> ddis = new ArrayList<>();
    ddis.add(1);
    subscriptionMessageParameters.ddis = ddis;
    subscriptionMessageParameters.technicalMessageType =
        TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    subscriptionMessageParameters.setPosition(true);
    ByteString message = this.getInstanceToTest().message(subscriptionMessageParameters);
    assertFalse(message.isEmpty());
  }

  @Test
  void givenEmptySubscriptionMessageParametersMessageShouldThrowException() {
    SubscriptionMessageParameters subscriptionMessageParameters =
        new SubscriptionMessageParameters();
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(subscriptionMessageParameters));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void givenSubscriptionMessageParametersWithNullValuesMessageShouldNotFail() {
    SubscriptionMessageParameters subscriptionMessageParameters =
        new SubscriptionMessageParameters();
    List<Integer> ddis = new ArrayList<>();
    ddis.add(1);
    subscriptionMessageParameters.ddis = null;
    subscriptionMessageParameters.technicalMessageType = null;
    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(subscriptionMessageParameters));
  }

  private SubscriptionMessageContentFactory getInstanceToTest() {
    return new SubscriptionMessageContentFactory();
  }
}
