package com.dke.data.agrirouter.test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Custom assertion adapter.
 */
public class Assertions extends org.junit.jupiter.api.Assertions {

    /**
     * Assert that the actual value matches any from the given expected values.
     *
     * @param expectedValues -
     * @param actual         -
     * @param <T>            -
     */
    public static <T> void assertMatchesAny(List<T> expectedValues, T actual) {
        var matches = new AtomicBoolean(false);
        assertNotNull(expectedValues);
        assertNotNull(actual);
        expectedValues.forEach(
                (expected) -> {
                    assertNotNull(expected);
                    if (expected.equals(actual)) {
                        matches.set(true);
                    }
                });
        assertTrue(matches.get(), "There should be at least a single match in the expected values.");
    }
}
