package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

/**
 * Parameter validation using bean validation.
 */
public interface ParameterValidation {

    Logger LOGGER = LogManager.getLogger();

    /**
     * Validation of the parameters. If there are any constraint violations, there will be a
     * exception.
     *
     * @throws IllegalParameterDefinitionException -
     */
    default void validate() {
        LOGGER.debug("Validating parameters.");
        LOGGER.trace("Technical validation.");
        this.technicalValidation();
        LOGGER.trace("Business validation.");
        this.businessValidation();
    }

    /**
     * Technical validation. Empty by default.
     */
    default void technicalValidation() {
        //
    }

    /**
     * Business validation. Empty by default.
     */
    default void businessValidation() {
        //
    }

    /**
     * Rise an exception if the parameter was not valid.
     */
    default void rise() {
        throw new IllegalParameterDefinitionException("Parameter was not defined correctly, please check the values.");
    }

    /**
     * Rise an exception if the parameter was not valid.
     *
     * @param message -
     */
    default void rise(String message) {
        throw new IllegalParameterDefinitionException(String.format("Parameter was not defined correctly, please check the values. Error message is '%s'.", message));
    }

    /**
     * Check for null values.
     *
     * @param o -
     */
    default void nullCheck(Object o) {
        if (null == o) {
            this.rise("The parameter should not have been null, please check your values.");
        }
    }

    /**
     * Check for null or empty values.
     *
     * @param s -
     */
    default void isBlank(String s) {
        if (StringUtils.isBlank(s)) {
            this.rise("The parameter should not have been null, please check your values.");
        }
    }

}
