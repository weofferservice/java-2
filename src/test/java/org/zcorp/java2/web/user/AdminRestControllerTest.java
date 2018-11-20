package org.zcorp.java2.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.User;
import org.zcorp.java2.util.UserUtil;
import org.zcorp.java2.web.AbstractControllerTest;

import java.util.Date;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.zcorp.java2.ErrorInfoTestData.contentValidationErrorInfoJson;
import static org.zcorp.java2.TestUtil.getContent;
import static org.zcorp.java2.TestUtil.userHttpBasic;
import static org.zcorp.java2.UserTestData.*;
import static org.zcorp.java2.web.validator.user.AbstractUserValidator.EMAIL_ALREADY_EXISTS;

public class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Test
    public void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + ADMIN_ID)
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isOk())
                //https://jira.spring.io/browse/SPR-14472
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(
                get(REST_URL + 1)
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetByEmail() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL + "by?email=" + USER.getEmail())
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(USER));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(REST_URL + USER_ID)
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(
                delete(REST_URL + 1)
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetAllUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAllForbidden() throws Exception {
        mockMvc.perform(
                get(REST_URL)
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdate() throws Exception {
        Date registeredDateExpected = userService.get(USER_ID).getRegistered();

        User updated = getUpdated();
        mockMvc.perform(
                put(REST_URL + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeJsonWithPassword(updated))
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User user = userService.get(USER_ID);
        assertMatch(user, UserUtil.toLowerCaseAndTrimEmail(updated));

        Date registeredDate = user.getRegistered();
        assertEquals(registeredDateExpected, registeredDate);
    }

    @Test
    public void testUpdateNotValid() throws Exception {
        User updated = getNotValidUpdated();
        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL + USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(updated))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertMatch(userService.get(USER_ID), USER);
    }

    @Test
    public void testUpdateOwnEmail() throws Exception {
        Date registeredDateExpected = userService.get(USER_ID).getRegistered();

        mockMvc.perform(
                put(REST_URL + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeJsonWithPassword(USER))
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User user = userService.get(USER_ID);

        assertMatch(user, USER);

        Date registeredDate = user.getRegistered();
        assertEquals(registeredDateExpected, registeredDate);
    }

    @Test
    public void testUpdateSomeoneElseEmail() throws Exception {
        User updated = getSomeoneElseEmailUpdated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        put(REST_URL + USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(updated))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertThat(getContent(action), containsString(
                messageSource.getMessage(EMAIL_ALREADY_EXISTS, null, Locale.getDefault())));

        assertMatch(userService.get(USER_ID), USER);
    }

    @Test
    public void testCreate() throws Exception {
        User expected = getCreated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(expected))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User returned = TestUtil.readFromJson(action, User.class);
        action.andExpect(redirectedUrlPattern("**" + REST_URL + returned.getId()));

        expected.setId(returned.getId());

        assertTrue(returned.getPassword() == null);

        returned.setPassword(expected.getPassword());
        assertMatch(returned, expected);
        MealTestData.assertMatch(returned.getMeals(), expected.getMeals());

        assertMatch(userService.getAll(), ADMIN, expected, USER);

        Date registeredDate = userService.get(returned.getId()).getRegistered();
        assertThat(registeredDate, greaterThan(expected.getRegistered()));
    }

    @Test
    public void testCreateNotValid() throws Exception {
        User created = getNotValidCreated();
        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(created))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    public void testCreateSomeoneElseEmail() throws Exception {
        User created = getSomeoneElseEmailCreated();
        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(created))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentValidationErrorInfoJson());

        assertThat(getContent(action), containsString(
                messageSource.getMessage(EMAIL_ALREADY_EXISTS, null, Locale.getDefault())));

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL)
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN, USER));
    }

}