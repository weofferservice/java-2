package org.zcorp.java2.util;

import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;

public class UsersUtil {
    public static final User USER = new User(null, "USER", "USER@gmail.com", "pass1", Role.ROLE_USER);
    public static final User ADMIN = new User(null, "ADMIN", "ADMIN@gmail.com", "pass2", Role.ROLE_ADMIN);
}
