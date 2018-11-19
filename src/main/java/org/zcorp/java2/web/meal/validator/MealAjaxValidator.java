package org.zcorp.java2.web.meal.validator;

import org.springframework.stereotype.Component;
import org.zcorp.java2.model.Meal;

@Component
public class MealAjaxValidator extends AbstractMealValidator {
    @Override
    protected Integer getId(Meal meal) {
        return meal.getId();
    }
}