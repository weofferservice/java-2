package org.zcorp.java2.web.meal.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;

import java.time.LocalDateTime;
import java.util.List;

import static org.zcorp.java2.web.SecurityUtil.authUserId;

public abstract class AbstractMealValidator implements Validator {
    public static final String DATETIME_ALREADY_EXISTS = "meal.datetimeAlreadyExists";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MealService service;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator defaultValidator;

    protected abstract Integer getId(Meal meal);

    private void checkDateTimeAlreadyExists(Integer id, LocalDateTime ldt, Errors errors) {
        List<Meal> meals = service.getBetweenDateTimes(ldt, ldt, authUserId());
        if (!meals.isEmpty()) {
            if (id == null || !id.equals(meals.get(0).getId())) {
                errors.rejectValue("dateTime", DATETIME_ALREADY_EXISTS,
                        messageSource.getMessage(DATETIME_ALREADY_EXISTS, null, LocaleContextHolder.getLocale()));
            }
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        defaultValidator.validate(target, errors);
        Meal meal = (Meal) target;
        LocalDateTime dateTime = meal.getDateTime();
        if (dateTime != null) {
            checkDateTimeAlreadyExists(getId(meal), dateTime, errors);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }
}