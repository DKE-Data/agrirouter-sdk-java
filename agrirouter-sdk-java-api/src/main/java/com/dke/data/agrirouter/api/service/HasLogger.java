package com.dke.data.agrirouter.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Interface to encapsulate logging capabilities. Logging will be done using LOG4J2, ruleset should
 * be:
 *
 * <ul>
 *   <li>Log method entry on level DEBUG.
 *   <li>Log method steps on level TRACE.
 *   <li>Log method result on level TRACE.
 *   <li>Log method exit on level DEBUG.
 * </ul>
 *
 * Please be aware of the rule "Log or throw" regarding exceptions.
 */
public interface HasLogger {

  Marker METHOD_BEGIN = MarkerFactory.getMarker("METHOD_BEGIN");
  Marker METHOD_END = MarkerFactory.getMarker("METHOD_END");

  /**
   * Returns the native LOG4J2 logger for further logging.
   *
   * @return -
   */
  default Logger getNativeLogger() {
    return LoggerFactory.getLogger(this.getClass());
  }

  /**
   * Log method begin. Will log all given parameters as well.
   *
   * @param objects The parameters of the method.
   */
  default void logMethodBegin(Object... objects) {
    getNativeLogger().debug(METHOD_BEGIN, "BEGIN | Start of method.");
  }

  /**
   * Log method exit. Will log all given results as well.
   *
   * @param objects The results of the method.
   */
  default void logMethodEnd(Object... objects) {
    getNativeLogger().debug(METHOD_END, "END | End of method.");
  }
}
