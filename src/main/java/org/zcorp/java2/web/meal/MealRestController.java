package org.zcorp.java2.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zcorp.java2.service.MealService;

@Controller
public class MealRestController extends AbstractMealController {
    @Autowired
    public MealRestController(MealService service) {
        super(service);
    }
}