package com.dke.data.agrirouter.api.exception;

import com.dke.data.agrirouter.api.service.ParameterValidation;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/** Will be thrown if any kind of parameter attribute is invalid due to the given metrics. */
public class IllegalParameterDefinitionException extends RuntimeException {

  private String errors;

  public IllegalParameterDefinitionException() {
    this(Collections.emptySet());
  }

  public IllegalParameterDefinitionException(String errors) {
    this(Collections.emptySet());
    this.errors = errors;
  }

  public IllegalParameterDefinitionException(Set<ConstraintViolation<ParameterValidation>> errors) {
    if (null != errors) {
      this.errors =
          errors.stream()
              .map(
                  e ->
                      "Validation error for property '"
                          + e.getPropertyPath()
                          + "' --> "
                          + e.getMessage())
              .collect(Collectors.joining("\n"));
    } else {
      this.errors = "";
    }
  }

  @Override
  public String getMessage() {
    return "There are validation errors. Please check your parameters.\n\n" + errors;
  }
}
