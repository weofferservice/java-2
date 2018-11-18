package org.zcorp.java2.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.UserUtil;
import org.zcorp.java2.web.AbstractControllerTest;
import org.zcorp.java2.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zcorp.java2.ErrorInfoTestData.contentValidationErrorInfoJson;
import static org.zcorp.java2.TestUtil.userHttpBasic;
import static org.zcorp.java2.UserTestData.*;
import static org.zcorp.java2.web.user.ProfileRestController.REST_URL;

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
                .andExpect(contentValidationErrorInfoJson());

        assertMatch(userService.get(USER_ID), USER);
    }

}