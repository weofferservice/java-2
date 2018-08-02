package org.zcorp.java2.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.to.MealWithExceed;
import org.zcorp.java2.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.zcorp.java2.web.SecurityUtil.authUserCaloriesPerDay;
import static org.zcorp.java2.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void create(Meal meal) {
        service.create(meal, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        meal.setId(id);
        service.update(meal, authUserId());
    }

    public List<MealWithExceed> getFilteredWithExceeded(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(service.getAll(authUserId()), authUserCaloriesPerDay(),
                startDate, endDate,
                startTime, endTime);
    }
}