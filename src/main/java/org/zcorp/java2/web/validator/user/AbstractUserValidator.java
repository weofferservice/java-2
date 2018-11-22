package org.zcorp.java2.web.validator.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.zcorp.java2.HasId;
import org.zcorp.java2.service.UserService;
import org.zcorp.java2.util.exception.NotFoundException;
import org.zcorp.java2.web.validator.AbstractValidator;
import org.zcorp.java2.web.validator.MessageUtil;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class AbstractUserValidator<T> extends AbstractValidator {

    public static final String EMAIL_ALREADY_EXISTS = "user.emailAlreadyExists";

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("defaultValidator")
    private SmartValidator defaultValidator;

    protected abstract Integer getId(T target);

    protected abstract String getEmail(T target);

    private void checkEmailAlreadyExists(Integer id, String email, Errors errors) {
        if (isEmpty(email) || isEmpty(email.trim())) {
            return;
        }
        try {
            HasId hasId = service.getByEmail(email);
            if (id == null || !id.equals(hasId.getId())) {
                errors.rejectValue(
                        "email",
                        EMAIL_ALREADY_EXISTS,
                        messageUtil.getMessage(EMAIL_ALREADY_EXISTS));
            }
        } catch (NotFoundException ignored) {
        }
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        defaultValidator.validate(target, errors, validationHints);
        checkEmailAlreadyExists(getId((T) target), getEmail((T) target), errors);
    }

}