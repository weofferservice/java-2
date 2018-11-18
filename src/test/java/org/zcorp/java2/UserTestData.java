package org.zcorp.java2;

import org.springframework.test.web.servlet.ResultMatcher;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.zcorp.java2.MealTestData.ADMIN_MEALS;
import static org.zcorp.java2.MealTestData.MEALS;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;
import static org.zcorp.java2.web.json.JsonUtil.writeAdditionProps;
import static org.zcorp.java2.web.json.JsonUtil.writeIgnoreProps;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER;
    public static final User ADMIN;

    static {
        USER = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.ROLE_USER);
        ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ROLE_ADMIN, Role.ROLE_USER);

        USER.setMeals(MEALS);
        ADMIN.setMeals(ADMIN_MEALS);
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
                .isEqualToIgnoringGivenFields(expected, "registered", "password");
    }

    public static User getCreated() {
        return new User(null, "New", "new@gmail.com", "newPassword", 2300, Role.ROLE_USER, Role.ROLE_ADMIN);
    }

    public static User getNotValidCreated() {
        return new User(null, null, null, null, null, null, null, EnumSet.of(Role.ROLE_USER, Role.ROLE_ADMIN));
    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setName("updatedName");
        updated.setEmail("updatedEmail@ya.ru");
        updated.setPassword("updatedPassword");
        updated.setEnabled(!USER.isEnabled());
        updated.setCaloriesPerDay(USER.getCaloriesPerDay() + 1);
        updated.setRegistered(new Date());
        updated.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        return updated;
    }

    public static User getNotValidUpdated() {
        User updated = new User(USER);
        updated.setName(null);
        updated.setEmail(null);
        updated.setPassword(null);
        updated.setCaloriesPerDay(null);
        updated.setEnabled(null);
        updated.setRegistered(null);
        updated.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        return updated;
    }

    public static UserTo getUpdatedTo() {
        return new UserTo(USER_ID, "updatedName", "updatedEmail@ya.ru", "updatedPassword", 1500);
    }

    public static UserTo getNotValidUpdatedTo() {
        return new UserTo(USER_ID, null, null, null, 0);
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "password", "meals");
    }

    public static void assertMatchWithRegisteredField(User actual, User expected) {
        assertThat(actual)
                //actual   - это java.sql.Timestamp (дочка java.util.Date)
                //expected - это java.util.Date
                /*
                  AssertJ, когда доходит до поля registered, вызывает метод equals:
                  1) Если бы он вызывал этот метод у expected-объекта, т.е. у объекта java.util.Date, то AssertJ признал бы объекты равными,
                     т.к. объект java.util.Date умеет корректно сравнивать себя со своими дочками
                  2) Но, т.к. на самом деле AssertJ вызывает метод equals у actual-объекта, т.е. у объекта java.sql.Timestamp, то AssertJ
                     считает, что объекты не равны, т.к. объект java.sql.Timestamp не умеет сравнивать себя со своим родителем java.util.Date
                     (баг java.sql.Timestamp-а)
                  Поэтому нужно определить свой компаратор для поля registered
                 */
                .usingComparatorForFields(new Comparator<Date>() {
                    @Override
                    public int compare(Date actual, Date expected) {
                        /*
                          Но и здесь не все так просто:
                          1) Если вызвать метод compareTo у expected-объекта, т.е. у объекта java.util.Date, то он проводит сравнение
                             по полю fastTime, которое по задумке авторов объекта java.util.Date хранит Epoch-время, т.е. миллисекунды,
                             начиная с 1 января 1970 года. Однако сравнение в этом случае пройдет неудачно, т.к. авторы дочернего класса
                             java.sql.Timestamp сменили концепцию и хранят там тоже Epoch-время, но с точностью до секунды, а не миллисекунды
                             (может это баг java.sql.Timestamp, а может и баг java.util.Date, т.к. по определению Epoch-времени оно должно
                             задаваться именно с точностью до секунды, а не миллисекунды, как сделали разработчики класса java.util.Date)
                          2) Поэтому нужно вызвать метод compareTo у actual-объекта, т.е. у объекта java.sql.Timestamp, т.к. в данном случае
                             разработчики предусмотрели работу класса java.sql.Timestamp со своим родителем java.util.Date
                         */
                        if (actual != null && expected != null) {
                            return actual.compareTo(expected);
                        }
                        if (actual == null && expected == null) {
                            return 0;
                        }
                        return expected == null ? 1 : -1;
                    }
                }, "registered")
                .isEqualToIgnoringGivenFields(expected, "password", "meals");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password", "meals").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return content().json(writeIgnoreProps(Arrays.asList(expected), "registered", "password", "meals"));
    }

    public static ResultMatcher contentJson(User expected) {
        return content().json(writeIgnoreProps(expected, "registered", "password", "meals"));
    }

    public static String writeJsonWithPassword(User user) {
        return writeAdditionProps(user, "password", user.getPassword());
    }
}
