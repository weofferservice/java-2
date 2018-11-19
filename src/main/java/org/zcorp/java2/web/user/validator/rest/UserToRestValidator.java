package org.zcorp.java2.web.user.validator.rest;

import org.springframework.stereotype.Component;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.web.user.validator.UserToValidator;

import static org.zcorp.java2.web.SecurityUtil.authUserId;

@Component
public class UserToRestValidator extends UserToValidator {
    @Override
    protected Integer getId(UserTo userTo) {
        return authUserId();
    }
}
