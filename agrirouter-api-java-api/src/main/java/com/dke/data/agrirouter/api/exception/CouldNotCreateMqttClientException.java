package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the MQTT service could not create a client. */
public class CouldNotCreateMqttClientException extends RuntimeException {

  public CouldNotCreateMqttClientException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
