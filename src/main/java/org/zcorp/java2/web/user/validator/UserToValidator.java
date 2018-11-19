package org.zcorp.java2.web.user.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.zcorp.java2.service.UserService;
import org.zcorp.java2.to.UserTo;

@Component
public class UserToValidator extends AbstractUserValidator<UserTo> {
    @Autowired
    public UserToValidator(UserService service, @Qualifier("defaultValidator") Validator defaultValidator) {
        super(service, defaultValidator);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    protected Integer getId(UserTo userTo) {
        return userTo.getId();
    }

    @Override
    protected String getEmail(UserTo userTo) {
        return userTo.getEmail();
    }
}
