package com.dke.data.agrirouter.api.factories.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dke.data.agrirouter.api.enums.TechnicalMessageType;
import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import com.dke.data.agrirouter.api.factories.impl.parameters.SubscriptionMessageParameters;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SubscriptionMessageContentFactoryTest {

  @Test
  void givenValidSubscriptionMessageParametersMessageShouldNotFail() {
    SubscriptionMessageParameters.SubscriptionMessageEntry subscriptionMessageEntry =
        new SubscriptionMessageParameters.SubscriptionMessageEntry();
    List<Integer> ddis = new ArrayList<>();
    ddis.add(1);
    subscriptionMessageEntry.setDdis(ddis);
    subscriptionMessageEntry.technicalMessageType = TechnicalMessageType.ISO_11783_TASKDATA_ZIP;
    subscriptionMessageEntry.setPosition(true);

    SubscriptionMessageParameters subscriptionMessageParameters =
        new SubscriptionMessageParameters();
    subscriptionMessageParameters.getList().add(subscriptionMessageEntry);

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
    SubscriptionMessageParameters.SubscriptionMessageEntry subscriptionMessageParametersEntry =
        new SubscriptionMessageParameters.SubscriptionMessageEntry();
    List<Integer> ddis = new ArrayList<>();
    ddis.add(1);
    subscriptionMessageParametersEntry.technicalMessageType = null;

    SubscriptionMessageParameters subscriptionMessageParameters =
        new SubscriptionMessageParameters();

    subscriptionMessageParameters.getList().add(subscriptionMessageParametersEntry);

    assertThrows(
        IllegalParameterDefinitionException.class,
        () -> this.getInstanceToTest().message(subscriptionMessageParameters));
  }

  private SubscriptionMessageContentFactory getInstanceToTest() {
    return new SubscriptionMessageContentFactory();
  }
}
