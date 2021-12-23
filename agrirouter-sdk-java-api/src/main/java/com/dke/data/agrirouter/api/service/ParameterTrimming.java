package com.dke.data.agrirouter.api.service;

/**
 * Interface to trim the parameters before the validation and later the usage of the values starts.
 */
public interface ParameterTrimming {

    /**
     * Trim all parameters to remove whitespaces and avoid problems during sending messages.
     */
    void trim();
}
