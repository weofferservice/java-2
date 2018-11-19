package org.zcorp.java2.web.user.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.zcorp.java2.HasId;
import org.zcorp.java2.service.UserService;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class AbstractUserValidator<T> implements Validator {

    private final UserService service;
    private final Validator defaultValidator;

    protected AbstractUserValidator(UserService service, Validator defaultValidator) {
        this.service = service;
        this.defaultValidator = defaultValidator;
    }

    protected abstract Integer getId(T target);

    protected abstract String getEmail(T target);

    private void checkEmailAlreadyExists(Integer id, String email, Errors errors) {
        if (isEmpty(email) || isEmpty(email.trim())) {
            return;
        }
        try {
            HasId hasId = service.getByEmail(email);
            if (id == null || !id.equals(hasId.getId())) {
                errors.rejectValue("email", "user.emailAlreadyExists");
            }
        } catch (NotFoundException ignored) {
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        defaultValidator.validate(target, errors);
        checkEmailAlreadyExists(getId((T) target), getEmail((T) target), errors);
    }

}