package org.zcorp.java2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.MealRepository;
import org.zcorp.java2.util.exception.NotFoundException;

import java.time.LocalDateTime;
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
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
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
    public Meal getWithUser(int id, int userId) throws NotFoundException {
        return checkNotFound(repository.getWithUser(id, userId), createErrorMessage(id, userId));
    }

    @Override
    public void update(Meal meal, int userId) throws NotFoundException {
        Assert.notNull(meal, "meal must not be null");
        checkNotFound(repository.save(meal, userId), createErrorMessage(meal, userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime must not be null");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    private static String createErrorMessage(Integer id, int userId) {
        return "id=" + id + " & userId=" + userId;
    }

    private static String createErrorMessage(Meal meal, int userId) {
        return createErrorMessage(meal == null ? null : meal.getId(), userId);
    }

}