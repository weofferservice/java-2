package org.zcorp.java2.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.MealRepository;
import org.zcorp.java2.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId={}", meal, userId);
        if (meal.isNew()) { // Create
            meal.setId(counter.incrementAndGet());
        } else { // Update
            Meal oldMeal = repository.get(meal.getId());
            if (oldMeal == null || oldMeal.getUserId() != userId) {
                return null;
            }
        }
        meal.setUserId(userId);
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for userId={}", id, userId);
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return false;
        }
        repository.remove(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for userId={}", id, userId);
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(toList());
    }
}
