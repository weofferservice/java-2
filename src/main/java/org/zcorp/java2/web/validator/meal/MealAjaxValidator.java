package org.zcorp.java2.web.validator.meal;

import org.springframework.stereotype.Component;
import org.zcorp.java2.model.Meal;

@Component
public class MealAjaxValidator extends AbstractMealValidator {
    @Override
    protected Integer getId(Meal meal) {
        return meal.getId();
    }
}