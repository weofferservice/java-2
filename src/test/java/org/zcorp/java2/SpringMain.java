package org.zcorp.java2;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.MealWithExceed;
import org.zcorp.java2.web.meal.MealRestController;
import org.zcorp.java2.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.zcorp.java2.TestUtil.mockAuthorize;
import static org.zcorp.java2.UserTestData.ADMIN;
import static org.zcorp.java2.UserTestData.USER;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (GenericXmlApplicationContext parentAppCtx = new GenericXmlApplicationContext();
             ConfigurableWebApplicationContext appCtx = new XmlWebApplicationContext()) {
            parentAppCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            // load вызывается прямо перед refresh(), иначе будет ошибка
            parentAppCtx.load("spring/spring-app.xml", "spring/spring-db.xml");
            parentAppCtx.refresh();

            appCtx.setParent(parentAppCtx);
            appCtx.setServletContext(new MockServletContext());
            appCtx.setConfigLocation("spring/spring-mvc.xml");
            appCtx.refresh();

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            User user = adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            System.out.println();
            System.out.println(user);
            System.out.println();

            mockAuthorize(USER);
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            List<MealWithExceed> filteredMealsWithExceeded = mealRestController.getBetween(
                    LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                    LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            System.out.println();
            filteredMealsWithExceeded.forEach(System.out::println);
            System.out.println();

            Meal meal = mealRestController.create(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Еда", 1500));
            System.out.println();
            System.out.println(meal);
            System.out.println();

            mockAuthorize(ADMIN);
            Meal newMeal = new Meal(meal.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Еда", 1000);
            mealRestController.update(newMeal, newMeal.getId());
        }
    }
}
