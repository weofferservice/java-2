package org.zcorp.java2.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.web.AbstractControllerTest;
import org.zcorp.java2.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.UserTestData.USER;
import static org.zcorp.java2.UserTestData.USER_ID;
import static org.zcorp.java2.util.MealsUtil.getFilteredWithExceeded;
import static org.zcorp.java2.util.MealsUtil.getWithExceeded;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    void testCreate() throws Exception {
        Meal expected = getCreated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(expected)))
                        .andDo(print()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        action.andExpect(redirectedUrlPattern("**" + REST_URL + returned.getId()));

        expected.setId(returned.getId());
        assertMatch(returned, expected);

        assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(get(REST_URL + MEAL1_ID))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(
                put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, updated);
    }

    @Test
    void testGetBetween() throws Exception {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 30);
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 31);
        LocalTime startTime = LocalTime.of(12, 0, 0);
        LocalTime endTime = LocalTime.of(14, 0, 0);
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + "filter")
                                .param("startDateTime", LocalDateTime.of(startDate, startTime).format(ISO_LOCAL_DATE_TIME))
                                .param("endDateTime", LocalDateTime.of(endDate, endTime).format(ISO_LOCAL_DATE_TIME)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getFilteredWithExceeded(MEALS, USER.getCaloriesPerDay(), startTime, endTime)));
    }

    @Test
    void testGetAll() throws Exception {
        TestUtil.print(
                mockMvc.perform(get(REST_URL))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

}