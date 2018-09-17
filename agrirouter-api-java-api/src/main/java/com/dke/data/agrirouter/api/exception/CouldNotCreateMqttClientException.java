package com.dke.data.agrirouter.api.exception;

/** Will be thrown if the service can not create a MQTT client. */
public class CouldNotCreateMqttClientException extends RuntimeException {
  public CouldNotCreateMqttClientException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
