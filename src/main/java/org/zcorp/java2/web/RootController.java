package org.zcorp.java2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.util.MealsUtil;

import static org.zcorp.java2.web.SecurityUtil.authUserCaloriesPerDay;
import static org.zcorp.java2.web.SecurityUtil.authUserId;

@Controller
public class RootController {
    @Autowired
    private MealService mealService;

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        model.addAttribute("meals",
                MealsUtil.getWithExceeded(mealService.getAll(authUserId()), authUserCaloriesPerDay()));
        return "meals";
    }
}
