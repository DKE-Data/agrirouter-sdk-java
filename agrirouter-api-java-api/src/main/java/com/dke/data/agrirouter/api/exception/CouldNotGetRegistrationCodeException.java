package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if the registration code could not be obtained.
 */
public class CouldNotGetRegistrationCodeException extends RuntimeException {

    public CouldNotGetRegistrationCodeException(Throwable cause) {
        super(cause);
    }

    public CouldNotGetRegistrationCodeException(String message, Throwable cause) {
        super(message, cause);
    }


}
