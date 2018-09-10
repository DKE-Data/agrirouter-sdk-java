package com.dke.data.agrirouter.api.factories.impl;

import com.dke.data.agrirouter.api.factories.MessageContentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract class AbstractMessageContentFactoryTest<T extends MessageContentFactory> {

  @Test
  @SuppressWarnings({"unchecked", "ConfusingArgumentToVarargsMethod", "ConstantConditions"})
  void givenNullParameters_Message_ShouldThrowIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> this.getInstanceToTest().message(null));
  }

  protected abstract T getInstanceToTest();
}
