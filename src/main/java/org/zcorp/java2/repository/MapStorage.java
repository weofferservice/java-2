package org.zcorp.java2.repository;

import org.zcorp.java2.model.Meal;
import org.zcorp.java2.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapStorage implements Storage {
    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    {
        MealsUtil.getMeals().forEach(this::save);
    }

    @Override
    public void save(Meal meal) {
        int id = counter.getAndIncrement();
        meal.setId(id);
        storage.put(id, meal);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public List<Meal> getAllSorted() {
        List<Meal> sorted = new ArrayList<>(storage.values());
        sorted.sort(Comparator.comparing(Meal::getDateTime));
        return sorted;
    }
}
