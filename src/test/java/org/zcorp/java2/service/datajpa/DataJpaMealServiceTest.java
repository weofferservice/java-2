package org.zcorp.java2.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.AbstractJpaDataJpaMealServiceTest;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.Profiles.DATAJPA;
import static org.zcorp.java2.UserTestData.ADMIN;
import static org.zcorp.java2.UserTestData.ADMIN_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractJpaDataJpaMealServiceTest {

    @Test
    @Override
    public void getWithUser() {
//        assertMatchWithUser(service.getWithUser(MEAL1_ID, USER_ID), MEAL1);
        Meal adminMeal = service.getWithUser(ADMIN_MEAL1_ID, ADMIN_ID);
        assertMatch(adminMeal, ADMIN_MEAL1);
        UserTestData.assertMatch(adminMeal.getUser(), ADMIN);
    }

    @Test
    public void getWithUserNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }

}
