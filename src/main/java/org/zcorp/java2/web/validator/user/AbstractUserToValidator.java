package org.zcorp.java2.web.validator.user;

import org.zcorp.java2.to.UserTo;

public abstract class AbstractUserToValidator extends AbstractUserValidator<UserTo> {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    protected String getEmail(UserTo userTo) {
        return userTo.getEmail();
    }
}
