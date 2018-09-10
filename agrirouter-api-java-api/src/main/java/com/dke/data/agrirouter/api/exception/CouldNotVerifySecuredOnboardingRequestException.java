package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if the request can not be verified.
 */
public class CouldNotVerifySecuredOnboardingRequestException extends RuntimeException {

    public CouldNotVerifySecuredOnboardingRequestException(Exception cause) {
        super(cause);
    }

}
