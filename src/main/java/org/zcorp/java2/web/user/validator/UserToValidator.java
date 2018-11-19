package org.zcorp.java2.web.user.validator;

import org.springframework.stereotype.Component;
import org.zcorp.java2.to.UserTo;

@Component
public class UserToValidator extends AbstractUserValidator<UserTo> {
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
