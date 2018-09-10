package com.dke.data.agrirouter.impl;

import com.dke.data.agrirouter.api.env.Environment;

/**
 * Internal implementation for an environmental service.
 */
public abstract class EnvironmentalService {

    /**
     * The current environment.
     */
    protected final Environment environment;

    protected EnvironmentalService(Environment environment) {
        this.environment = environment;
    }
}
