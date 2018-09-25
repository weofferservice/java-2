package org.zcorp.java2.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.zcorp.java2.util.DateTimeUtil.parseLocalDate;
import static org.zcorp.java2.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping
    public String saveMeal(HttpServletRequest request) {
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            create(meal);
        } else {
            update(meal, meal.getId());
        }
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        int id = getId(request);
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String createMeal(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/update")
    public String updateMeal(HttpServletRequest request) {
        request.setAttribute("meal", get(getId(request)));
        request.setAttribute("action", "update");
        return "mealForm";
    }

    @GetMapping
    public String meals(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}
