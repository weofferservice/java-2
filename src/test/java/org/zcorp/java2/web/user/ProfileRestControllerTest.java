package org.zcorp.java2.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.zcorp.java2.MealTestData;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.UserUtil;
import org.zcorp.java2.web.AbstractControllerTest;
import org.zcorp.java2.web.json.JsonUtil;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.zcorp.java2.TestUtil.userHttpBasic;
import static org.zcorp.java2.UserTestData.*;
import static org.zcorp.java2.util.exception.ErrorType.VALIDATION_ERROR;
import static org.zcorp.java2.web.user.ProfileRestController.REST_URL;
import static org.zcorp.java2.web.validator.user.AbstractUserValidator.EMAIL_ALREADY_EXISTS;

public class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    public void testGet() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        get(REST_URL)
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(USER));
    }

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(REST_URL)
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    public void testRegister() throws Exception {
        User expected = getOrdinaryCreated();
        UserTo createdTo = UserUtil.asTo(expected);

        ResultActions action = TestUtil.print(
                mockMvc.perform(
                        post(REST_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(createdTo)))
                        .andDo(print()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User returned = TestUtil.readFromJson(action, User.class);
        action.andExpect(redirectedUrlPattern("**" + REST_URL + "/" + returned.getId()));

        expected.setId(returned.getId());

        assertTrue(returned.getPassword() == null);

        returned.setPassword(expected.getPassword());
        assertMatch(returned, expected);
        MealTestData.assertMatch(returned.getMeals(), expected.getMeals());

        assertMatch(userService.getAll(), ADMIN, expected, USER);
        assertMatch(userService.getByEmail(expected.getEmail()), expected);

        Date registeredDate = userService.get(returned.getId()).getRegistered();
        assertThat(registeredDate, greaterThan(expected.getRegistered()));
    }

    @Test
    public void testRegisterNotValid() throws Exception {
        User expected = getNotValidCreated();
        UserTo createdTo = UserUtil.asTo(expected);

        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(createdTo)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR));

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    public void testRegisterAuth() throws Exception {
        User expected = getOrdinaryCreated();
        UserTo createdTo = UserUtil.asTo(expected);
        mockMvc.perform(
                post(REST_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeJsonWithPassword(createdTo))
                        .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRegisterSomeoneElseEmail() throws Exception {
        User expected = getSomeoneElseEmailOrdinaryCreated();
        UserTo createdTo = UserUtil.asTo(expected);

        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(createdTo)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR))
                .andExpect(jsonErrorDetails(EMAIL_ALREADY_EXISTS));

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    public void testRegisterHtmlUnsafe() throws Exception {
        UserTo createdTo = getHtmlUnsafeCreatedTo();

        TestUtil.print(
                mockMvc.perform(
                        post(REST_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(createdTo)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR));

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    public void testUpdate() throws Exception {
        UserTo updatedTo = getUpdatedTo();
        mockMvc.perform(
                put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updatedTo))
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    public void testUpdateNotValid() throws Exception {
        UserTo updatedTo = getNotValidUpdatedTo();
        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(updatedTo))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR));

        assertMatch(userService.get(USER_ID), USER);
    }

    @Test
    public void testUpdateOwnEmail() throws Exception {
        Date registeredDateExpected = userService.get(USER_ID).getRegistered();

        UserTo updatedTo = getToFromUSER();
        mockMvc.perform(
                put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updatedTo))
                        .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User user = userService.get(USER_ID);

        assertMatch(user, USER);

        Date registeredDate = user.getRegistered();
        assertEquals(registeredDateExpected, registeredDate);
    }

    @Test
    public void testUpdateSomeoneElseEmail() throws Exception {
        UserTo updatedTo = getSomeoneElseEmailUpdatedTo();

        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValue(updatedTo))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR))
                .andExpect(jsonErrorDetails(EMAIL_ALREADY_EXISTS));

        assertMatch(userService.get(USER_ID), USER);
    }

    @Test
    public void testUpdateHtmlUnsafe() throws Exception {
        UserTo updatedTo = getHtmlUnsafeUpdatedTo();

        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(updatedTo))
                                .with(userHttpBasic(USER)))
                        .andDo(print()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(VALIDATION_ERROR));

        assertMatch(userService.get(USER_ID), USER);
    }

}