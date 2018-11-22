package org.zcorp.java2.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

public abstract class AbstractValidator implements SmartValidator {
    @Override
    public void validate(Object target, Errors errors) {
        throw new UnsupportedOperationException();
    }
}
