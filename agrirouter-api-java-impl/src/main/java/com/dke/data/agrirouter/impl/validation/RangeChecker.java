package com.dke.data.agrirouter.impl.validation;

import com.dke.data.agrirouter.api.exception.UnexpectedHttpStatusException;

class RangeChecker {

    static void checkRange(int actualStatus, int expectedStatus) {
        if (expectedStatus == 200) {
            if (actualStatus < 200 || actualStatus > 299) {
                throw new UnexpectedHttpStatusException(actualStatus, expectedStatus);
            }
        }
    }
}
