package com.dke.data.agrirouter.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;

public interface RequestLogging {

  Logger logger = LogManager.getLogger(RequestLogging.class);

  default void logRequest(String className, Response response) {
    String logMessage = "\n" + "# [" + className + "] " + "\n" + response + "\n";
    this.logger.info(() -> logMessage);
  }
}
