package com.dke.data.agrirouter.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface LoggingEnabledService {

    Logger LOGGER = LogManager.getLogger();

    default Logger getLogger(){
        return LOGGER;
    }

}
