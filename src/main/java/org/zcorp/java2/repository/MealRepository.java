package org.zcorp.java2.repository;

import org.zcorp.java2.model.Meal;

import java.util.List;

public interface MealRepository {
    // null if update and not found or if meal of other user
    Meal save(Meal meal, int userId);

    // false if not found or if meal of other user
    boolean delete(int id, int userId);

    // null if not found or if meal of other user
    Meal get(int id, int userId);

    List<Meal> getAll(int userId);
}
