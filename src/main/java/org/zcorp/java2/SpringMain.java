package org.zcorp.java2;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.web.SecurityUtil;
import org.zcorp.java2.web.meal.MealRestController;
import org.zcorp.java2.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.zcorp.java2.util.UsersUtil.USER_ID;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            User user = adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            System.out.println();
            System.out.println(user);
            System.out.println();

            SecurityUtil.setAuthUserId(user.getId());
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            Meal meal = mealRestController.create(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Еда", 1500));
            System.out.println();
            System.out.println(meal);
            System.out.println();

            SecurityUtil.setAuthUserId(USER_ID);
            Meal newMeal = new Meal(meal.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Еда", 1000);
            mealRestController.update(newMeal, newMeal.getId());
        }
    }
}
