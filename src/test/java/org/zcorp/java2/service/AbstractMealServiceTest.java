package org.zcorp.java2.service;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.util.exception.ErrorType;
import org.zcorp.java2.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.UserTestData.ADMIN_ID;
import static org.zcorp.java2.UserTestData.USER_ID;
import static org.zcorp.java2.util.exception.NotFoundException.EXCEPTION_NOT_FOUND;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    public void create() {
        Meal created = service.create(getCreated(), USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void get() {
        assertMatch(service.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    public void getNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> service.update(getUpdated(), ADMIN_ID));
        assertTrue(e.getType() == ErrorType.DATA_NOT_FOUND);
        assertTrue(e.getHttpStatus() == HttpStatus.UNPROCESSABLE_ENTITY);
        assertTrue(e.getMsgCode().equals(EXCEPTION_NOT_FOUND));
        assertTrue(e.getMsgArgs()[0].equals("id=" + MEAL1_ID + " & userId=" + ADMIN_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 30, 13, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 13, 0), USER_ID);
        assertMatch(meals, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(meals, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getWithUser() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> service.getWithUser(MEAL1_ID, USER_ID));
    }

    @Test
    public void testValidation() {
        Assumptions.assumeTrue(isJpaBased(), "Validation not supported (only for JPA)");
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "           ",  300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null,                                             null, "Description",  300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description",    9), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID), ConstraintViolationException.class);
    }

}