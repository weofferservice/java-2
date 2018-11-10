package org.zcorp.java2.util;

import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;

import java.util.Date;
import java.util.EnumSet;

public class UserUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static User createFromTo(UserTo userTo) {
        return new User(
                userTo.getId(), userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(),
                userTo.getCaloriesPerDay(), true, new Date(), EnumSet.of(Role.ROLE_USER)
        );
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getCaloriesPerDay());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        user.setCaloriesPerDay(userTo.getCaloriesPerDay());
        return user;
    }

}