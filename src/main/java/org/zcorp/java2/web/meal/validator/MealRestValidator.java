package org.zcorp.java2.web.meal.validator;

import org.springframework.stereotype.Component;
import org.zcorp.java2.model.Meal;

import static org.zcorp.java2.util.RequestUtil.getIdFromRequest;
import static org.zcorp.java2.web.meal.MealRestController.REST_URL;

@Component
public class MealRestValidator extends AbstractMealValidator {
    @Override
    protected Integer getId(Meal meal) {
        return getIdFromRequest(REST_URL);
    }
}