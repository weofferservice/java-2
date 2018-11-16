package org.zcorp.java2.util;

import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

public class UserUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private static final Field[] FIELDS =
            Stream.of(
                    User.class.getDeclaredFields(),
                    User.class.getSuperclass().getDeclaredFields()
            ).flatMap(Stream::of).toArray(Field[]::new);

    public static User createFromTo(UserTo userTo) {
        return new User(
                userTo.getId(), userTo.getName(), userTo.getEmail().toLowerCase(),
                userTo.getPassword(), userTo.getCaloriesPerDay(), Role.ROLE_USER
        );
    }

    public static User refreshRegisteredDate(User user) {
        user.setRegistered(new Date());
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getCaloriesPerDay());
    }

    public static User updateFrom(User user, User userFrom) {
        for (Field field : FIELDS) {
            int modifiers = field.getModifiers();
            if (!(isStatic(modifiers) && isFinal(modifiers))) {
                field.setAccessible(true);
                try {
                    Object fieldValueFrom = field.get(userFrom);
                    if (fieldValueFrom != null) {
                        field.set(user, fieldValueFrom);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return user;
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        user.setCaloriesPerDay(userTo.getCaloriesPerDay());
        return user;
    }

    public static UserTo createEmptyTo() {
        return new UserTo(null, null, null, null, null);
    }

}