package org.zcorp.java2;

import org.springframework.test.web.servlet.ResultMatcher;
import org.zcorp.java2.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.zcorp.java2.UserTestData.ADMIN;
import static org.zcorp.java2.UserTestData.USER;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;
import static org.zcorp.java2.web.json.JsonUtil.writeIgnoreProps;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL1_ID = START_SEQ + 8;

    public static final Meal MEAL1;
    public static final Meal MEAL2;
    public static final Meal MEAL3;
    public static final Meal MEAL4;
    public static final Meal MEAL5;
    public static final Meal MEAL6;
    public static final Meal ADMIN_MEAL1;
    public static final Meal ADMIN_MEAL2;

    public static final List<Meal> MEALS;
    public static final List<Meal> ADMIN_MEALS;

    static {
        MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        MEAL2 = new Meal(MEAL1_ID + 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        MEAL3 = new Meal(MEAL1_ID + 2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        MEAL4 = new Meal(MEAL1_ID + 3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        MEAL5 = new Meal(MEAL1_ID + 4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        MEAL6 = new Meal(MEAL1_ID + 5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        ADMIN_MEAL1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
        ADMIN_MEAL2 = new Meal(ADMIN_MEAL1_ID + 1, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

        MEALS = Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
        ADMIN_MEALS = Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1);

        MEALS.forEach(meal -> meal.setUser(USER));
        ADMIN_MEALS.forEach(meal -> meal.setUser(ADMIN));
    }

    public static Meal getCreated() {
        return new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 18, 0), "Созданный ужин", 300);
    }

    public static Meal getNotValidCreated() {
        return new Meal(null, "", 0);
    }

    public static Meal getDuplicateOwnDatetimeCreated() {
        Meal meal = getCreated();
        meal.setDateTime(MEAL1.getDateTime());
        return meal;
    }

    public static Meal getDuplicateSomeoneElseDatetimeCreated() {
        Meal meal = getCreated();
        meal.setDateTime(ADMIN_MEAL1.getDateTime());
        return meal;
    }

    public static Meal getHtmlUnsafeCreated() {
        Meal meal = getCreated();
        meal.setDescription("<script>alert('Description')</script>");
        return meal;
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Обновленный завтрак");
        updated.setCalories(200);
        return updated;
    }

    public static Meal getNotValidUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDateTime(null);
        updated.setDescription("");
        updated.setCalories(0);
        return updated;
    }

    public static Meal getDuplicateOwnDatetimeUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDateTime(MEAL2.getDateTime());
        return updated;
    }

    public static Meal getDuplicateSomeoneElseDatetimeUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDateTime(ADMIN_MEAL1.getDateTime());
        return updated;
    }

    public static Meal getHtmlUnsafeUpdated() {
        Meal meal = getUpdated();
        meal.setDescription("<script>alert('Description')</script>");
        return meal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user");
    }

    public static void assertMatchWithUser(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Meal expected) {
        return content().json(writeIgnoreProps(expected, "user"));
    }
}
