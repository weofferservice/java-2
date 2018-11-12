package org.zcorp.java2.web.meal;

import org.slf4j.Logger;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.to.MealWithExceed;
import org.zcorp.java2.util.DateTimeUtil;
import org.zcorp.java2.util.MealsUtil;
import org.zcorp.java2.web.AbstractController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.Util.orElse;
import static org.zcorp.java2.util.ValidationUtil.assureIdConsistent;
import static org.zcorp.java2.util.ValidationUtil.checkNew;
import static org.zcorp.java2.web.SecurityUtil.authUserCaloriesPerDay;
import static org.zcorp.java2.web.SecurityUtil.authUserId;

public abstract class AbstractMealController extends AbstractController {
    private final Logger log = getLogger(getClass());

    private final MealService service;

    public AbstractMealController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        int userId = authUserId();
        log.info("create {} for userId={}", meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete mealId={} for userId={}", id, userId);
        service.delete(id, userId);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get mealId={} for userId={}", id, userId);
        return service.get(id, userId);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        int userId = authUserId();
        log.info("update {} for userId={}", meal, userId);
        service.update(meal, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealWithExceed> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for userId={}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenDates(
                orElse(startDate, DateTimeUtil.MIN_DATE), orElse(endDate, DateTimeUtil.MAX_DATE),
                userId);

        return MealsUtil.getFilteredWithExceeded(mealsDateFiltered, authUserCaloriesPerDay(),
                orElse(startTime, LocalTime.MIN), orElse(endTime, LocalTime.MAX));
    }

    public List<MealWithExceed> getAll() {
        int userId = authUserId();
        log.info("getAll for userId={}", userId);
        return MealsUtil.getWithExceeded(service.getAll(userId), authUserCaloriesPerDay());
    }
}
