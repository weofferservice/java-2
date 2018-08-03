package org.zcorp.java2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.MealRepository;
import org.zcorp.java2.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.zcorp.java2.util.ValidationUtil.checkNotFound;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) throws NotFoundException {
        return checkNotFound(repository.save(meal, userId), createErrorMessage(meal, userId));
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFound(repository.delete(id, userId), createErrorMessage(id, userId));
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFound(repository.get(id, userId), createErrorMessage(id, userId));
    }

    @Override
    public void update(Meal meal, int userId) throws NotFoundException {
        checkNotFound(repository.save(meal, userId), createErrorMessage(meal, userId));
    }

    @Override
    public List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.getFiltered(startDate, endDate, userId);
    }

    private static String createErrorMessage(Integer id, int userId) {
        return "id=" + id + " for userId=" + userId;
    }

    private static String createErrorMessage(Meal meal, int userId) {
        return createErrorMessage(meal == null ? null : meal.getId(), userId);
    }

}