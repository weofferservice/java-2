package org.zcorp.java2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.UserTestData.ADMIN_ID;
import static org.zcorp.java2.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = service.create(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Новая еда", 1000), USER_ID);
        assertMatch(service.getAll(USER_ID), newMeal, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(USER_MEAL_1_ID, ADMIN_ID);
    }

    @Test
    public void get() {
        assertMatch(service.get(USER_MEAL_1_ID, USER_ID), USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(USER_MEAL_1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setCalories(1000);
        updated.setDescription("Обновленная еда");
        service.update(updated, USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setCalories(1000);
        updated.setDescription("Обновленная еда");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 30, 13, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 13, 0), USER_ID);
        assertMatch(meals, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(meals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

}