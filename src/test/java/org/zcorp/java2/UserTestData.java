package org.zcorp.java2;

import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER;
    public static final User ADMIN;

    static {
        USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
        ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN, Role.ROLE_USER);

        USER.setMeals(MEALS);
        ADMIN.setMeals(Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));
    }

    public static void assertMatchWithMeals(User actual, User expected) {
        // Когда Hibernate достает из БД список, то он создает объект PersistentBag.
        // Однако PersistentBag хоть и является списком, в нем не переопределен метод equals,
        // поэтому результат сравнения PersistentBag с обычным списком всегда вернет false.
        // Чтобы этого избежать нужно задать свой компаратор, где actualMeals - это PersistentBag,
        // а expectedMeals - тестовый список, который специально передается в метод Objects.equals
        // первым аргументом, чтобы сравнение списков проходило правильно.
        // В противном случае работать не будет
        assertThat(actual)
                .usingComparatorForFields((actualMeals, expectedMeals) -> Objects.equals(expectedMeals, actualMeals) ? 0 : 1, "meals")
                .isEqualToIgnoringGivenFields(expected, "registered");
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "meals");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "meals").isEqualTo(expected);
    }
}
