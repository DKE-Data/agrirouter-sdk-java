package com.dke.data.agrirouter.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/**
 * Interface to encapsulate logging capabilities. Logging will be done using LOG4J2, ruleset should
 * be:
 *
 * <ul>
 *   <li>Log method entry on level DEBUG.
 *   <li>Log method parameters on level TRACE.
 *   <li>Log method steps on level TRACE.
 *   <li>Log method result on level TRACE.
 *   <li>Log method exit on level DEBUG.
 * </ul>
 *
 * Please be aware of the rule "Log or throw" regarding exceptions.
 */
public interface LoggingEnabledService {

  Marker METHOD_BEGIN = MarkerManager.getMarker("METHOD_BEGIN");
  Marker METHOD_END = MarkerManager.getMarker("METHOD_END");
  Marker METHOD_PARAMETERS = MarkerManager.getMarker("METHOD_PARAMETERS");
  Marker METHOD_RESULTS = MarkerManager.getMarker("METHOD_RESULTS");

  Logger LOGGER = LogManager.getLogger();

  /**
   * Returns the native LOG4J2 logger for further logging.
   *
   * @return -
   */
  default Logger getNativeLogger() {
    return LOGGER;
  }

  /**
   * Log method begin. Will log all given parameters as well.
   *
   * @param objects The parameters of the method.
   */
  default void logMethodBegin(Object... objects) {
    LOGGER.debug(METHOD_BEGIN, "BEGIN | Start of method.");
    LOGGER.trace(METHOD_PARAMETERS, new ObjectArrayMessage(objects));
  }

  /**
   * Log method exit. Will log all given results as well.
   *
   * @param objects The results of the method.
   */
  default void logMethodEnd(Object... objects) {
    LOGGER.debug(METHOD_END, "END | End of method.");
    LOGGER.trace(METHOD_RESULTS, new ObjectArrayMessage(objects));
  }
}
