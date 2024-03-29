package com.dke.data.agrirouter.impl.validation;

import com.dke.data.agrirouter.api.exception.ForbiddenRequestException;
import com.dke.data.agrirouter.api.exception.InvalidHttpStatusException;
import com.dke.data.agrirouter.api.exception.InvalidUrlForRequestException;
import com.dke.data.agrirouter.api.exception.UnauthorizedRequestException;
import com.dke.data.agrirouter.api.service.HasLogger;
import org.apache.http.HttpStatus;

/**
 * Validation of the response, encapsulated using an interface.
 */
public interface ResponseValidator extends HasLogger {

    /**
     * Asserting that the status code is valid. A valid status is in between 200 and 207 (defined by
     * HTTP).
     *
     * @param statusCode The current status code.
     */
    @SuppressWarnings("UnusedReturnValue")
    default boolean assertStatusCodeIsValid(int statusCode) {
        getNativeLogger().debug("Validating status code.");
        getNativeLogger().trace("Status code was '{}'", statusCode);
        this.checkIfStatusCodeIsError(statusCode);
        if (statusCode != HttpStatus.SC_OK
                && statusCode != HttpStatus.SC_CREATED
                && statusCode != HttpStatus.SC_ACCEPTED
                && statusCode != HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION
                && statusCode != HttpStatus.SC_NO_CONTENT
                && statusCode != HttpStatus.SC_RESET_CONTENT
                && statusCode != HttpStatus.SC_PARTIAL_CONTENT
                && statusCode != HttpStatus.SC_MULTI_STATUS) {
            throw new InvalidHttpStatusException(statusCode);
        }
        return true;
    }

    /**
     * Asserting that the status code is HTTP OK.
     *
     * @param statusCode The current status code.
     */
    @SuppressWarnings("UnusedReturnValue")
    default boolean assertStatusCodeIsOk(int statusCode) {
        getNativeLogger().debug("Validating status code.");
        getNativeLogger().trace("Status code was '{}'", statusCode);
        this.checkIfStatusCodeIsError(statusCode);
        if (statusCode != HttpStatus.SC_OK) {
            throw new InvalidHttpStatusException(statusCode);
        }
        return true;
    }

    /**
     * Asserting that the status code is HTTP CREATED.
     *
     * @param statusCode The current status code.
     */
    default boolean assertStatusCodeIsCreated(int statusCode) {
        getNativeLogger().debug("Validating status code.");
        getNativeLogger().trace("Status code was '{}'", statusCode);
        this.checkIfStatusCodeIsError(statusCode);
        if (statusCode != HttpStatus.SC_CREATED) {
            throw new InvalidHttpStatusException(statusCode);
        }
        return true;
    }

    /**
     * Asserting that the status code is HTTP BAD REQUEST.
     *
     * @param statusCode The current status code.
     */
    @SuppressWarnings("unused")
    default boolean assertStatusCodeIsBadRequest(int statusCode) {
        getNativeLogger().debug("Validating status code.");
        getNativeLogger().trace("Status code was '{}'", statusCode);
        this.checkIfStatusCodeIsError(statusCode);
        if (statusCode != HttpStatus.SC_BAD_REQUEST) {
            throw new InvalidHttpStatusException(statusCode);
        }
        return true;
    }

    /**
     * Will check if the status code is an error. If there will an error a business exception will
     * rise.
     *
     * @param statusCode The current response.
     */
    default void checkIfStatusCodeIsError(int statusCode) {
        getNativeLogger().debug("Checking if the response is an error.");
        if (statusCode == HttpStatus.SC_NOT_FOUND) {
            throw new InvalidUrlForRequestException();
        }
        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            throw new UnauthorizedRequestException();
        }
        if (statusCode == HttpStatus.SC_FORBIDDEN) {
            throw new ForbiddenRequestException();
        }
    }
}
