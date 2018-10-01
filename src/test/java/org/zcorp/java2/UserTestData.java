package org.zcorp.java2;

import org.springframework.test.web.servlet.ResultMatcher;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;
import static org.zcorp.java2.web.json.JsonUtil.writeIgnoreProps;

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

    public static User getCreated() {
        return new User(null, "New", "new@gmail.com", "newPassword", Role.ROLE_USER, Role.ROLE_ADMIN);
    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setName("updatedName");
        updated.setEmail("updatedEmail@ya.ru");
        updated.setPassword("updatedPassword");
        updated.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        return updated;
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "meals");
    }

    public static void assertMatchWithRegisteredField(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "meals");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "meals").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return content().json(writeIgnoreProps(Arrays.asList(expected), "registered", "meals"));
    }

    public static ResultMatcher contentJson(User expected) {
        return content().json(writeIgnoreProps(expected, "registered", "meals"));
    }
}
