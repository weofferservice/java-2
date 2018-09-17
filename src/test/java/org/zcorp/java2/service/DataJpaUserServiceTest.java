package org.zcorp.java2.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.model.User;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.zcorp.java2.UserTestData.*;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    @Override
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        assertMatchWithMeals(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealsNotFound() {
        service.getWithMeals(1);
    }

}
