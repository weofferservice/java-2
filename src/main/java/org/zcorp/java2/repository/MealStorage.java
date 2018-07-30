package org.zcorp.java2.repository;

import org.zcorp.java2.model.Meal;

import java.util.List;

public interface MealStorage {

    void save(Meal meal);

    Meal get(int id);

    void update(Meal meal);

    void delete(int id);

    List<Meal> getAllSorted();

}
