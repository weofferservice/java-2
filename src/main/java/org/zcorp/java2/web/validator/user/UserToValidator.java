package org.zcorp.java2.web.validator.user;

import org.springframework.stereotype.Component;
import org.zcorp.java2.to.UserTo;

@Component
public class UserToValidator extends AbstractUserToValidator {
    @Override
    protected Integer getId(UserTo userTo) {
        return userTo.getId();
    }
}
