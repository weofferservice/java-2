package org.zcorp.java2.web.json;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.model.User;

import java.util.Arrays;
import java.util.List;

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
        UserTestData.assertMatchWithRegisteredField(meal.getUser(), ADMIN);

        String jsonUser = JsonUtil.writeValue(ADMIN);
        log.info("jsonUser =\n" + jsonUser);
        User user = JsonUtil.readValue(jsonUser, User.class);
        UserTestData.assertMatchWithRegisteredField(user, ADMIN);
        MealTestData.assertMatch(user.getMeals(), ADMIN_MEALS);
    }

    @Test
    public void writeIgnorePropsReadValue() {
        String jsonUser = JsonUtil.writeIgnoreProps(ADMIN, "registered");
        log.info("jsonUser =\n" + jsonUser);
        User user = JsonUtil.readValue(jsonUser, User.class);
        UserTestData.assertMatch(user, ADMIN);
        MealTestData.assertMatch(user.getMeals(), ADMIN_MEALS);
    }

    @Test
    public void writeReadValues() {
        String jsonMeals = JsonUtil.writeValue(ADMIN_MEALS);
        log.info("jsonMeals =\n" + jsonMeals);
        List<Meal> meals = JsonUtil.readValues(jsonMeals, Meal.class);
        MealTestData.assertMatch(meals, ADMIN_MEALS);
        meals.stream().map(Meal::getUser)
                .forEach(
                        user -> UserTestData.assertMatchWithRegisteredField(user, ADMIN)
                );
    }

    @Test
    public void writeIgnorePropsReadValues() {
        List<User> expected = Arrays.asList(USER, ADMIN);
        String jsonUsers = JsonUtil.writeIgnoreProps(expected, "registered");
        log.info("jsonUsers =\n" + jsonUsers);
        List<User> users = JsonUtil.readValues(jsonUsers, User.class);
        UserTestData.assertMatch(users, expected);
        MealTestData.assertMatch(users.get(0).getMeals(), MEALS);
        MealTestData.assertMatch(users.get(1).getMeals(), ADMIN_MEALS);
    }

}