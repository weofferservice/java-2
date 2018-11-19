package org.zcorp.java2.web.user.validator.rest;

import org.springframework.stereotype.Component;
import org.zcorp.java2.model.User;
import org.zcorp.java2.web.user.validator.AbstractUserValidator;

import static org.zcorp.java2.util.RequestUtil.getIdFromRequest;
import static org.zcorp.java2.web.user.AdminRestController.REST_URL;

@Component
public class UserRestValidator extends AbstractUserValidator<User> {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    protected Integer getId(User user) {
        return getIdFromRequest(REST_URL);
    }

    @Override
    protected String getEmail(User user) {
        return user.getEmail();
    }
}
