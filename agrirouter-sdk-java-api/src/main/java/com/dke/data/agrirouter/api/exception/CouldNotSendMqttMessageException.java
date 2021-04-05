package com.dke.data.agrirouter.api.exception;

/** Exception to rise if the MQTT message could not be send. */
public class CouldNotSendMqttMessageException extends RuntimeException {

  public CouldNotSendMqttMessageException(Throwable cause) {
    super(cause);
  }
}
