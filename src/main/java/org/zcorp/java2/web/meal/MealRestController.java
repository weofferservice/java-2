package org.zcorp.java2.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.to.MealWithExceed;
import org.zcorp.java2.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.ValidationUtil.assureIdConsistent;
import static org.zcorp.java2.util.ValidationUtil.checkNew;
import static org.zcorp.java2.web.SecurityUtil.authUserCaloriesPerDay;
import static org.zcorp.java2.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public List<MealWithExceed> getFilteredWithExceeded(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getFilteredWithExceeded with startDate={}, endDate={}, startTime={}, endTime={} for userId={}", startDate, endDate, startTime, endTime, authUserId());
        return MealsUtil.getFilteredWithExceeded(service.getFiltered(startDate, endDate, authUserId()), authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<MealWithExceed> getAllWithExceeded() {
        log.info("getAllWithExceeded");
        return getFilteredWithExceeded(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
    }
}