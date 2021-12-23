package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Parameter validation using bean validation. */
public interface ParameterValidation extends ParameterTrimming {

  Logger LOGGER = LogManager.getLogger();

  /**
   * Validation of the parameters. If there are any constraint violations, there will be a
   * exception.
   *
   * @throws IllegalParameterDefinitionException -
   */
  default void trimAndValidate() {
    LOGGER.debug("Validating parameters.");
    LOGGER.trace("Trim the values.");
    this.trim();
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
   * Rise an exception if there has to be at least one valid parameter.
   *
   * @param message -
   */
  default void rise(String message, String... parameterNames) {
    final String joinedParametersNames = String.join(",", parameterNames);
    throw new IllegalParameterDefinitionException(
        String.format(
            "At least one of the following parameters has to be defined [%s] was not defined correctly, please check the values. '%s'.",
            joinedParametersNames, message));
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
