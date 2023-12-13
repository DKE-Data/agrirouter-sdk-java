package com.dke.data.agrirouter.impl.messaging;

import com.dke.data.agrirouter.api.dto.onboard.OnboardingResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SequenceNumberServiceTest {

    @Test
    void givenOnboardingResponseWhenGeneratingSequenceNumberForTheFirstTimeThenTheResultShouldBe1() {
        var onboardingResponse = new OnboardingResponse();
        onboardingResponse.setSensorAlternateId("this-is-my-id");
        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
    }

    @Test
    void
    givenOnboardingResponseWhenGeneratingSequenceNumberForTwoTimesInARowThenTheResultShouldBe1And2() {
        var onboardingResponse = new OnboardingResponse();
        onboardingResponse.setSensorAlternateId("this-is-another-id");
        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
        Assertions.assertEquals(
                2, SequenceNumberService.generateSequenceNumberForEndpoint(onboardingResponse));
    }

    @Test
    void
    givenTwoOnboardingResponsesWhenGeneratingSequenceNumberForTheFirstTimeThenTheResultShouldBe1ForBoth() {
        var firstOnboardingResponse = new OnboardingResponse();
        firstOnboardingResponse.setSensorAlternateId("this-is-the-first-id");
        var secondOnboardingResponse = new OnboardingResponse();
        secondOnboardingResponse.setSensorAlternateId("this-is-the-second-id");
        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));
        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(secondOnboardingResponse));
    }

    @Test
    void
    givenTwoOnboardingResponsesWhenGeneratingSequenceNumberForMultipleTimesThenTheResultShouldBeMatchingTheTimesTheMethodWasCalled() {
        var firstOnboardingResponse = new OnboardingResponse();
        firstOnboardingResponse.setSensorAlternateId("this-is-the-first-id-for-multiple-calls");
        var secondOnboardingResponse = new OnboardingResponse();
        secondOnboardingResponse.setSensorAlternateId("this-is-the-second-id-multiple-calls");
        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));
        Assertions.assertEquals(
                2, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));
        Assertions.assertEquals(
                3, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));
        Assertions.assertEquals(
                4, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));

        Assertions.assertEquals(
                1, SequenceNumberService.generateSequenceNumberForEndpoint(secondOnboardingResponse));
        Assertions.assertEquals(
                2, SequenceNumberService.generateSequenceNumberForEndpoint(secondOnboardingResponse));
        Assertions.assertEquals(
                3, SequenceNumberService.generateSequenceNumberForEndpoint(secondOnboardingResponse));

        Assertions.assertEquals(
                5, SequenceNumberService.generateSequenceNumberForEndpoint(firstOnboardingResponse));
    }
}
