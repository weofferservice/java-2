package org.zcorp.java2.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.service.AbstractMealServiceTest;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.Profiles.DATAJPA;
import static org.zcorp.java2.UserTestData.ADMIN_ID;
import static org.zcorp.java2.UserTestData.USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    @Override
    public void getWithUser() {
        assertMatchWithUser(service.getWithUser(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    public void getWithUserNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }

}
