package org.zcorp.java2.service;

import org.zcorp.java2.model.Meal;
import org.zcorp.java2.util.exception.NotFoundException;

import java.util.List;

public interface MealService {

    Meal create(Meal meal, int userId) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    void update(Meal meal, int userId) throws NotFoundException;

    List<Meal> getAll(int userId);

}