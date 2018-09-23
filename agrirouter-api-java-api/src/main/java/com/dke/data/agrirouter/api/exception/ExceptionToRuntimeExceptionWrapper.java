package com.dke.data.agrirouter.api.exception;

/**
 * This class is used to transport Exceptions, that must be caught from within a position, where you
 * cannot throw something (like a foreach loop) To handle the exception outside that ForEachLoop
 */
public class ExceptionToRuntimeExceptionWrapper extends RuntimeException {
  public ExceptionToRuntimeExceptionWrapper(Exception e) {
    super(e);
  }
}
