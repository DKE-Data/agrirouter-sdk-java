package com.dke.data.agrirouter.impl.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeCheckerTest {

    @ParameterizedTest
    @ValueSource(ints = {200, 201, 202, 203, 204, 205, 206, 207, 208, 226})
    void givenSuccessStatus_CheckStatusInSuccessRange_ShouldReturnTrue(int status) {
        assertTrue(ResponseStatusChecker.isStatusInSuccessRange(status));
    }

    @Test
    void givenAnyStatus2XX_CheckStatusInSuccessRange_ShouldReturnTrue() {
        for (int actualStatus = 200; actualStatus <= 299; actualStatus++) {
            assertTrue(ResponseStatusChecker.isStatusInSuccessRange(actualStatus));
        }
    }

    @Test
    void givenOtherStatus_CheckStatusInSuccessRange_ShouldReturnFalse() {
        assertFalse(ResponseStatusChecker.isStatusInSuccessRange(404));
    }
}
