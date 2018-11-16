package org.zcorp.java2.web.json;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.model.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.UserTestData.ADMIN;
import static org.zcorp.java2.UserTestData.USER;

public class JsonUtilTest {

    private static final Logger log = getLogger(JsonUtilTest.class);

    @Test
    public void writeReadValue() {
        String jsonMeal = JsonUtil.writeValue(ADMIN_MEAL1);
        log.info("jsonMeal =\n" + jsonMeal);
        Meal meal = JsonUtil.readValue(jsonMeal, Meal.class);
        MealTestData.assertMatch(meal, ADMIN_MEAL1);
        assertTrue(meal.getUser().getPassword() == null);
        meal.getUser().setPassword(ADMIN.getPassword());
        UserTestData.assertMatchWithRegisteredField(meal.getUser(), ADMIN);

        String jsonUser = JsonUtil.writeValue(ADMIN);
        log.info("jsonUser =\n" + jsonUser);
        User user = JsonUtil.readValue(jsonUser, User.class);
        assertTrue(user.getPassword() == null);
        user.setPassword(ADMIN.getPassword());
        UserTestData.assertMatchWithRegisteredField(user, ADMIN);
        MealTestData.assertMatch(user.getMeals(), ADMIN_MEALS);
    }

    @Test
    public void writeIgnorePropsReadValue() {
        String jsonUser = JsonUtil.writeIgnoreProps(ADMIN, "registered");
        log.info("jsonUser =\n" + jsonUser);
        User user = JsonUtil.readValue(jsonUser, User.class);
        assertTrue(user.getPassword() == null);
        user.setPassword(ADMIN.getPassword());
        UserTestData.assertMatch(user, ADMIN);
        MealTestData.assertMatch(user.getMeals(), ADMIN_MEALS);
    }

    @Test
    public void writeReadValues() {
        String jsonMeals = JsonUtil.writeValue(ADMIN_MEALS);
        log.info("jsonMeals =\n" + jsonMeals);
        List<Meal> meals = JsonUtil.readValues(jsonMeals, Meal.class);
        MealTestData.assertMatch(meals, ADMIN_MEALS);
        User user = meals.get(0).getUser();
        assertTrue(user.getPassword() == null);
        user.setPassword(ADMIN.getPassword());
        UserTestData.assertMatchWithRegisteredField(user, ADMIN);
        meals.stream().map(Meal::getUser).forEach(u -> assertTrue(u == user));
    }

    @Test
    public void writeIgnorePropsReadValues() {
        List<User> expected = Arrays.asList(USER, ADMIN);
        String jsonUsers = JsonUtil.writeIgnoreProps(expected, "registered");
        log.info("jsonUsers =\n" + jsonUsers);
        List<User> users = JsonUtil.readValues(jsonUsers, User.class);
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(users.get(i).getPassword() == null);
            users.get(i).setPassword(expected.get(i).getPassword());
        }
        UserTestData.assertMatch(users, expected);
        MealTestData.assertMatch(users.get(0).getMeals(), MEALS);
        MealTestData.assertMatch(users.get(1).getMeals(), ADMIN_MEALS);
    }

    @Test
    public void writeOnlyAccess() {
        String jsonWithoutPass = JsonUtil.writeValue(USER);
        log.info("jsonUserWithoutPass =\n" + jsonWithoutPass);
        assertThat(jsonWithoutPass, not(containsString("password")));

        String jsonWithPass = UserTestData.writeJsonWithPassword(USER);
        log.info("jsonUserWithPass =\n" + jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        UserTestData.assertMatch(user, USER);
    }

}