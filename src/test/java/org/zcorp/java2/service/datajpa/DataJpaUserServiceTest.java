package org.zcorp.java2.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.service.AbstractJpaDataJpaUserServiceTest;
import org.zcorp.java2.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zcorp.java2.MealTestData.ADMIN_MEAL1;
import static org.zcorp.java2.MealTestData.ADMIN_MEAL2;
import static org.zcorp.java2.Profiles.DATAJPA;
import static org.zcorp.java2.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractJpaDataJpaUserServiceTest {

    @Test
    @Override
    public void getWithMeals() {
        User admin = service.getWithMeals(ADMIN_ID);
//        assertMatchWithMeals(admin, ADMIN);
        assertMatch(admin, ADMIN);
        MealTestData.assertMatch(admin.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> service.getWithMeals(1));
    }

}
