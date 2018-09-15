package com.dke.data.agrirouter.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.ObjectArrayMessage;

public interface LoggingEnabledService {

    Marker METHOD_BEGIN = MarkerManager.getMarker("METHOD_BEGIN");
    Marker METHOD_END = MarkerManager.getMarker("METHOD_END");
    Marker METHOD_PARAMETERS = MarkerManager.getMarker("METHOD_PARAMETERS");
    Marker METHOD_RESULTS = MarkerManager.getMarker("METHOD_RESULTS");

    Logger LOGGER = LogManager.getLogger();

    default Logger getNativeLogger() {
        return LOGGER;
    }

    default void logMethodBegin() {
        LOGGER.debug(METHOD_BEGIN, "BEGIN | Start of method.");
    }

    default void logParameters(Object... objects) {
        LOGGER.trace(METHOD_PARAMETERS, new ObjectArrayMessage(objects));
    }

    default void logResult(Object object) {
        LOGGER.trace(METHOD_RESULTS, new ObjectArrayMessage(object));
    }

    default void logMethodEnd() {
        LOGGER.debug(METHOD_END, "END | End of method.");
    }

}
