package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the MQTT option can not be created. */
public class CouldNotCreateMqttOptionException extends RuntimeException {

  public CouldNotCreateMqttOptionException(String message) {
    super(message);
  }

  public CouldNotCreateMqttOptionException(String message, Throwable cause) {
    super(message, cause);
  }
}
