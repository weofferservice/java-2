package org.zcorp.java2.repository;

import org.zcorp.java2.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    // null if updating meal not found or do not belong to userId
    Meal save(Meal meal, int userId);

    // false if meal not found or if meal do not belong to userId
    boolean delete(int id, int userId);

    // null if meal not found or if meal do not belong to userId
    Meal get(int id, int userId);

    // null if meal not found or if meal do not belong to userId
    default Meal getWithUser(int id, int userId) {
        throw new UnsupportedOperationException();
    }

    // ORDERED BY dateTime DESC
    List<Meal> getAll(int userId);

    // ORDERED BY dateTime DESC
    List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}