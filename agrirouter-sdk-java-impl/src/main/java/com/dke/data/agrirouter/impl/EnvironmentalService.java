package com.dke.data.agrirouter.impl;

import com.dke.data.agrirouter.api.env.Environment;
import com.dke.data.agrirouter.api.service.LoggingEnabledService;

/** Internal implementation for an environmental service. */
public abstract class EnvironmentalService implements LoggingEnabledService {

  /** The current environment. */
  protected final Environment environment;

  protected EnvironmentalService(Environment environment) {
    this.environment = environment;
  }
}
