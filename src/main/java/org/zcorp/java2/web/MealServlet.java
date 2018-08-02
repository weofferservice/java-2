package org.zcorp.java2.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.repository.UserRepository;
import org.zcorp.java2.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.web.SecurityUtil.authUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private UserRepository userRepository;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        userRepository = appCtx.getBean(UserRepository.class);
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");

        Integer id = idStr.isEmpty() ? null : Integer.valueOf(idStr);
        Meal meal = new Meal(id,
                id == null ? authUserId() : userRepository.get(id).getId(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update {}", meal);
            mealRestController.update(meal, id);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getFilteredWithExceeded");

                String startDateStr = request.getParameter("startDate");
                LocalDate startDate = startDateStr.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDateStr);

                String endDateStr = request.getParameter("endDate");
                LocalDate endDate = endDateStr.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDateStr);

                String startTimeStr = request.getParameter("startTime");
                LocalTime startTime = startTimeStr.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTimeStr);

                String endTimeStr = request.getParameter("endTime");
                LocalTime endTime = endTimeStr.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTimeStr);

                request.setAttribute("meals", mealRestController.getFilteredWithExceeded(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
