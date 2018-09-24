package org.zcorp.java2.service;

import org.junit.Test;
import org.zcorp.java2.model.Meal;

import javax.validation.ConstraintViolationException;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.zcorp.java2.UserTestData.USER_ID;

public abstract class AbstractJpaDataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void testValidation() {
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "           ",  300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null,                                             null, "Description",  300), USER_ID), NullPointerException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description",    9), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID), ConstraintViolationException.class);
    }

}
