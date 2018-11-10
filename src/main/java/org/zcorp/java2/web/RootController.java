package org.zcorp.java2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;

import static org.zcorp.java2.web.SecurityUtil.*;

@Controller
public class RootController {
    @Autowired
    private MealService mealService;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        setAuthUserId(userId);
        return "redirect:meals";
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        model.addAttribute("meals",
                MealsUtil.getWithExceeded(mealService.getAll(authUserId()), authUserCaloriesPerDay()));
        return "meals";
    }
}
