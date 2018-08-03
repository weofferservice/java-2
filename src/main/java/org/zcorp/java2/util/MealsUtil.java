package org.zcorp.java2.util;

import org.zcorp.java2.model.Meal;
import org.zcorp.java2.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static org.zcorp.java2.util.UsersUtil.USER_ID;
import static org.zcorp.java2.util.UsersUtil.ADMIN_ID;

public class MealsUtil {
    public static final List<Meal> MEALS;
    static {
        Meal meal1 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        meal1.setUserId(USER_ID);
        Meal meal2 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        meal2.setUserId(USER_ID);
        Meal meal3 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        meal3.setUserId(USER_ID);
        Meal meal4 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        meal4.setUserId(ADMIN_ID);
        Meal meal5 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        meal5.setUserId(ADMIN_ID);
        Meal meal6 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        meal6.setUserId(ADMIN_ID);
        MEALS = Arrays.asList(meal1, meal2, meal3, meal4, meal5, meal6);
    }

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    private static MealWithExceed createWithExceed(Meal meal, boolean exceed) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
    }

    // TODO остальные методы применить при реализации паттерна Стратегия (после окончания проекта)

    public static List<MealWithExceed> getFilteredWithExceededByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealWithExceed> mealsWithExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                mealsWithExceeded.add(
                        createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)
                );
            }
        });
        return mealsWithExceeded;
    }

    public static List<MealWithExceed> getFilteredWithExceededInOnePass(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Collection<List<Meal>> list = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate)).values();

        return list.stream()
                .flatMap(dayMeals -> {
                            boolean exceed = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                            return dayMeals.stream()
                                    .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                                    .map(meal -> createWithExceed(meal, exceed));
                        }
                )
                .collect(toList());
    }

    public static List<MealWithExceed> getFilteredWithExceededInOnePass2(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class Aggregate {
            private final List<Meal> dailyMeals = new ArrayList<>();
            private int dailySumOfCalories;

            private void accumulate(Meal meal) {
                dailySumOfCalories += meal.getCalories();
                if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                    dailyMeals.add(meal);
                }
            }

            /**
             * Метод combine вызывается только для parallelStream, когда нужно склеить результаты,
             * полученные в параллельных потоках по принципу ForkJoin-фреймворка (Map-Reduce)
             */
            private Aggregate combine(Aggregate that) {
                this.dailySumOfCalories += that.dailySumOfCalories;
                this.dailyMeals.addAll(that.dailyMeals);
                return this;
            }

            private Stream<MealWithExceed> finisher() {
                final boolean exceed = dailySumOfCalories > caloriesPerDay;
                return dailyMeals.stream().map(meal -> createWithExceed(meal, exceed));
            }
        }

        Collection<Stream<MealWithExceed>> values = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate,
                                Collector.of(Aggregate::new, Aggregate::accumulate, Aggregate::combine, Aggregate::finisher)))
                .values();

        return values.stream().flatMap(identity()).collect(toList());
    }

    public static void main(String[] args) {
        List<MealWithExceed> mealsWithExceeded = getFilteredWithExceeded(MEALS, 2000, LocalTime.of(7, 0), LocalTime.of(12, 0));
        mealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredWithExceededByCycle(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededInOnePass(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededInOnePass2(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }
}
