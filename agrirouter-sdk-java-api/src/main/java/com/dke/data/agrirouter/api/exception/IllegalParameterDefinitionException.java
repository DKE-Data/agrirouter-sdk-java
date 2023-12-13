package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if any kind of parameter attribute is invalid due to the given metrics.
 */
public class IllegalParameterDefinitionException extends RuntimeException {

    private final String message;

    public IllegalParameterDefinitionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "There are validation errors. Please check your parameters.\n\n" + message;
    }
}
