package org.zcorp.java2.web;

import org.slf4j.Logger;

import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.MapMealStorage;
import org.zcorp.java2.repository.MealStorage;
import org.zcorp.java2.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final MealStorage mealStorage = new MapMealStorage();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String datetime = request.getParameter("datetime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        Meal meal = new Meal(LocalDateTime.parse(datetime), description, Integer.parseInt(calories));
        if (id.length() > 0) {
            meal.setId(Integer.parseInt(id));
            mealStorage.update(meal);
        } else {
            mealStorage.save(meal);
        }
        log.debug("redirect to meals from doPost");
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealsWithExceed",
                    MealsUtil.getFilteredWithExceeded(mealStorage.getAllSorted(), LocalTime.MIN, LocalTime.MAX, 2000));
            log.debug("forward to meals from doGet");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        String id = request.getParameter("id");
        switch (action) {
            case "delete":
                mealStorage.delete(parseIdForAction(id, action));
                log.debug("redirect to meals from doGet");
                response.sendRedirect("meals");
                return;
            case "update":
                Meal meal = mealStorage.get(parseIdForAction(id, action));
                request.setAttribute("meal", meal);
                break;
            case "create":
                break;
            default:
                throw new IllegalStateException("Action " + action + " is not supported");
        }
        log.debug("forward to edit_meal from doGet");
        request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
    }

    private int parseIdForAction(String id, String action) {
        if (id == null) {
            throw new IllegalStateException("Action " + action + " must have id");
        }
        return Integer.parseInt(id);
    }
}
