package org.zcorp.java2.web.user.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.zcorp.java2.HasId;
import org.zcorp.java2.service.UserService;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class AbstractUserValidator<T> implements Validator {

    private static final String EMAIL_ALREADY_EXISTS = "user.emailAlreadyExists";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator defaultValidator;

    protected abstract Integer getId(T target);

    protected abstract String getEmail(T target);

    private void checkEmailAlreadyExists(Integer id, String email, Errors errors) {
        if (isEmpty(email) || isEmpty(email.trim())) {
            return;
        }
        try {
            HasId hasId = service.getByEmail(email.toLowerCase());
            if (id == null || !id.equals(hasId.getId())) {
                errors.rejectValue(
                        "email",
                        EMAIL_ALREADY_EXISTS,
                        messageSource.getMessage(EMAIL_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()));
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