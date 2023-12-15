package com.dke.data.agrirouter.api.exception;

/**
 * Will be thrown if the keystore can not be created.
 */
public class CouldNotCreateDynamicKeyStoreException extends RuntimeException {

    public CouldNotCreateDynamicKeyStoreException(Exception cause) {
        super(cause);
    }
}
