package com.dke.data.agrirouter.api.service;

import javax.ws.rs.core.Response;

public interface RequestLogging extends HasLogger {

  default void logRequest(String className, Response response) {
    String logMessage = "\n" + "# [" + className + "] " + "\n" + response + "\n";
    this.getNativeLogger().info(logMessage);
  }
}
