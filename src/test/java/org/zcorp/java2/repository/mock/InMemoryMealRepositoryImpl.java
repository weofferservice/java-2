package org.zcorp.java2.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.MealRepository;
import org.zcorp.java2.util.DateTimeUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.UserTestData.ADMIN_ID;
import static org.zcorp.java2.UserTestData.USER_ID;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);

    // Map userId -> (mealId -> meal)
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(START_SEQ + 9);

    {
        Map<Integer, Meal> meals = repository.computeIfAbsent(USER_ID, ConcurrentHashMap::new);
        MEALS.forEach(meal -> meals.put(meal.getId(), meal));

        Map<Integer, Meal> adminMeals = repository.computeIfAbsent(ADMIN_ID, ConcurrentHashMap::new);
        adminMeals.put(ADMIN_MEAL1.getId(), ADMIN_MEAL1);
        adminMeals.put(ADMIN_MEAL2.getId(), ADMIN_MEAL2);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("+++ PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("+++ PreDestroy");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId={}", meal, userId);
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for userId={}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for userId={}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        log.info("getBetween with startDateTime={} and endDateTime={} for userId={}", startDateTime, endDateTime, userId);
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(toList());
    }
}