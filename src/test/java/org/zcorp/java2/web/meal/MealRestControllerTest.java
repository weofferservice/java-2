package org.zcorp.java2.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.web.AbstractControllerTest;
import org.zcorp.java2.web.json.JsonUtil;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.zcorp.java2.ErrorInfoTestData.contentValidationErrorInfoJson;
import static org.zcorp.java2.MealTestData.*;
import static org.zcorp.java2.TestUtil.*;
import static org.zcorp.java2.UserTestData.USER;
import static org.zcorp.java2.UserTestData.USER_ID;
import static org.zcorp.java2.util.MealsUtil.createWithExceed;
import static org.zcorp.java2.util.MealsUtil.getWithExceeded;
import static org.zcorp.java2.web.meal.validator.AbstractMealValidator.DATETIME_ALREADY_EXISTS;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService service;

    @Test
    void testCreate() throws Exception {
        Meal expected = getCreated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(expected))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        action.andExpect(redirectedUrlPattern("**" + REST_URL + returned.getId()));

        expected.setId(returned.getId());
        assertMatch(returned, expected);

        assertMatch(service.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testCreateNotValid() throws Exception {
        Meal created = getNotValidCreated();
        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(created))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    void testCreateDuplicateOwnDatetime() throws Exception {
        Meal created = getDuplicateOwnDatetimeCreated();
        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(created))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson())
                .andExpect(content().string(containsString(
                        messageSource.getMessage(DATETIME_ALREADY_EXISTS, null, Locale.ENGLISH))));

        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    void testCreateDuplicateSomeoneElseDatetime() throws Exception {
        Meal expected = getDuplicateSomeoneElseDatetimeCreated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(expected))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        action.andExpect(redirectedUrlPattern("**" + REST_URL + returned.getId()));

        expected.setId(returned.getId());
        assertMatch(returned, expected);

        assertMatch(service.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(
                delete(REST_URL + MEAL1_ID)
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(
                delete(REST_URL + ADMIN_MEAL1_ID)
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + MEAL1_ID)
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(
                get(REST_URL + ADMIN_MEAL1_ID)
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(
                put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testUpdateNotValid() throws Exception {
        Meal updated = getNotValidUpdated();
        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL + MEAL1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(updated))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertMatch(service.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    void testUpdateCurrentDatetime() throws Exception {
        mockMvc.perform(
                put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(MEAL1))
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    void testUpdateDuplicateOwnDatetime() throws Exception {
        Meal updated = getDuplicateOwnDatetimeUpdated();
        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL + MEAL1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(updated))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson())
                .andExpect(content().string(containsString(
                        messageSource.getMessage(DATETIME_ALREADY_EXISTS, null, Locale.ENGLISH))));

        assertMatch(service.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test
    void testUpdateDuplicateSomeoneElseDatetime() throws Exception {
        Meal updated = getDuplicateSomeoneElseDatetimeUpdated();
        mockMvc.perform(
                put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testGetBetween() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + "filter?startDate=2015-05-30&startTime=07:00&endDate=2015-05-31&endTime=11:00")
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonArray(
                        createWithExceed(MEAL4, true),
                        createWithExceed(MEAL1, false)));
    }

    @Test
    void testGetBetweenAll() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + "filter?startDate=&endTime=")
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonCollection(getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void testGetAll() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL)
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonCollection(getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

}