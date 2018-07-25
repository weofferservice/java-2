package org.zcorp.java2.util;

import org.zcorp.java2.model.Meal;
import org.zcorp.java2.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
        );
    }

    public static List<MealWithExceed> getFilteredWithExceeded(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        Map<LocalDate, List<Meal>> mealMap = new HashMap<>();
        for (Meal meal : mealList) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesPerDayMap.merge(localDate, meal.getCalories(), (x, y) -> x + y);
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealMap.computeIfAbsent(localDate, key -> new LinkedList<>()).add(meal);
            }
        }
        List<MealWithExceed> mealWithExceeds = new LinkedList<>();
        List<Meal> emptyList = Collections.emptyList();
        for (Map.Entry<LocalDate, Integer> e : caloriesPerDayMap.entrySet()) {
            for (Meal meal : mealMap.getOrDefault(e.getKey(), emptyList)) {
                mealWithExceeds.add(
                        new MealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), e.getValue() > caloriesPerDay));
            }
        }
        return mealWithExceeds;
    }
}
