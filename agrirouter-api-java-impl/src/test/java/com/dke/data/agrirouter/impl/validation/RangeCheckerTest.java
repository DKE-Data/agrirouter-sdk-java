package com.dke.data.agrirouter.impl.validation;

import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RangeCheckerTest {

    @Test
    void givenExpectedStatus_CheckRange_ShouldFinishWithoutException() {
        try {
            RangeChecker.checkRange(200, HttpStatus.SC_OK);
        } catch (UnexpectedHttpStatusException e) {
            Assertions.fail("actual and expected status match, should not throw exception");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {201, 202, 203, 204, 205, 206, 207})
    void givenStatusInAcceptableRange_CheckRange_ShouldFinishWithoutException(int actualStatus) {
        try {
            RangeChecker.checkRange(actualStatus, HttpStatus.SC_OK);
        } catch (UnexpectedHttpStatusException e) {
            Assertions.fail("actual status in acceptable range, should not throw exception");
        }
    }

    @Test
    void givenUnexpectedStatusOutOfRange_CheckRange_ShouldThrowException() {
        Assertions.assertThrows(
                UnexpectedHttpStatusException.class,
                () -> RangeChecker.checkRange(404, HttpStatus.SC_OK));
    }
}
