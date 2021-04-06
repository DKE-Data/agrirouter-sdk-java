package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Parameter validation using bean validation. */
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

  /** Technical validation. Empty by default. */
  default void technicalValidation() {
    //
  }

  /** Business validation. Empty by default. */
  default void businessValidation() {
    //
  }

  /**
   * Rise an exception if the parameter was not valid.
   *
   * @param message -
   */
  default void rise(String message, String parameterName) {
    throw new IllegalParameterDefinitionException(
        String.format(
            "Parameter '%s' was not defined correctly, please check the values. Error message is '%s'.",
            parameterName, message));
  }

  /**
   * Check for null values.
   *
   * @param o -
   */
  default void nullCheck(String parameterName, Object o) {
    if (null == o) {
      this.rise(
          "The parameter '%s' should not have been null, please check your values.", parameterName);
    }
  }

  /**
   * Check for null or empty values.
   *
   * @param s -
   */
  default void isBlank(String parameterName, String s) {
    if (StringUtils.isBlank(s)) {
      this.rise(
          "The parameter '%s' should not have been blank, please check your values.",
          parameterName);
    }
  }

  /**
   * Check for null or empty values.
   *
   * @param c -
   */
  default void nullOrEmpty(String parameterName, Collection<?> c) {
    nullCheck(parameterName, c);
    if (c.isEmpty()) {
      this.rise(
          "The parameter '%s' should not have been empty, please check your values.",
          parameterName);
    }
  }
}
