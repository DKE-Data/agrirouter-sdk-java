package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
   * rise an exception if the parameter was not valid.
   * @param parameterName
   */
  default void throwException(String parameterName){
    throw new IllegalParameterDefinitionException(String.format("Parameter '%s' was not defined correctly, please check the values."));
  }

}
