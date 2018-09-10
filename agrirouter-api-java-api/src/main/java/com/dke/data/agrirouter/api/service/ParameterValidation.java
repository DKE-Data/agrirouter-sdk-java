package com.dke.data.agrirouter.api.service;

import com.dke.data.agrirouter.api.exception.IllegalParameterDefinitionException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Parameter validation using bean validation.
 */
public interface ParameterValidation {

    /**
     * Validation of the parameters. If there are any constraint violations, there will be a exception.
     *
     * @throws IllegalParameterDefinitionException -
     */
    default void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ParameterValidation>> validate = validator.validate(this);
        if (validate.size() > 0) {
            throw new IllegalParameterDefinitionException(validate);
        }
    }


}
