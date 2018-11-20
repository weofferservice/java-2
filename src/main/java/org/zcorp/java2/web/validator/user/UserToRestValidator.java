package org.zcorp.java2.web.validator.user;

import org.springframework.stereotype.Component;
import org.zcorp.java2.AuthorizedUser;
import org.zcorp.java2.to.UserTo;

import static org.zcorp.java2.web.SecurityUtil.safeGet;

@Component
public class UserToRestValidator extends AbstractUserToValidator {
    @Override
    protected Integer getId(UserTo userTo) {
        AuthorizedUser user = safeGet();
        if (user == null) {
            return null;
        }
        return user.getUserTo().getId();
    }
}
