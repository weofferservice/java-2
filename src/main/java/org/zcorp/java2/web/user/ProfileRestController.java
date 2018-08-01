package org.zcorp.java2.web.user;

import org.springframework.stereotype.Controller;
import org.zcorp.java2.model.User;

import static org.zcorp.java2.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }

}