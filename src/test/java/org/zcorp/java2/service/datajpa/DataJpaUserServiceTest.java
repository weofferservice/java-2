package org.zcorp.java2.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.service.AbstractUserServiceTest;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.zcorp.java2.MealTestData.MEALS;
import static org.zcorp.java2.Profiles.DATAJPA;
import static org.zcorp.java2.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    @Override
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
//        assertMatchWithMeals(user, USER);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), MEALS);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealsNotFound() {
        service.getWithMeals(1);
    }

}
