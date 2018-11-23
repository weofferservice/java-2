package org.zcorp.java2.web.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.TestUtil;
import org.zcorp.java2.model.User;
import org.zcorp.java2.web.AbstractControllerTest;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zcorp.java2.Profiles.HEROKU;
import static org.zcorp.java2.TestUtil.userHttpBasic;
import static org.zcorp.java2.UserTestData.*;
import static org.zcorp.java2.util.exception.ErrorType.APP_ERROR;
import static org.zcorp.java2.util.exception.ModificationRestrictionException.EXCEPTION_MODIFICATION_RESTRICTION;

@ActiveProfiles({HEROKU})
public class HerokuAdminRestControllerTest extends AbstractControllerTest {

    private static final Logger log = getLogger(HerokuAdminRestControllerTest.class);

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @BeforeAll
    static void prepareToEmulateHerokuProfile() {
        // Set environment variable DATABASE_URL to emulate heroku profile
        Resource resource = new ClassPathResource("db/postgres.properties");

        PropertySource propertySource;
        try {
            propertySource = new ResourcePropertySource(resource);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        String herokuDbUrl = String.format(
                "postgresql://%s:%s@%s",
                propertySource.getProperty("database.username"),
                propertySource.getProperty("database.password"),
                ((String) propertySource.getProperty("database.url")).substring(18));

        log.info("herokuDbUrl = " + herokuDbUrl);

        System.setProperty("DATABASE_URL", herokuDbUrl);
    }

    @Test
    void testDeleteModificationRestriction() throws Exception {
        TestUtil.print(
                mockMvc.perform(
                        delete(REST_URL + USER_ID)
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnavailableForLegalReasons())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(APP_ERROR))
                .andExpect(jsonErrorDetails(EXCEPTION_MODIFICATION_RESTRICTION, USER.getName()));

        assertMatch(userService.getAll(), ADMIN, USER);
    }

    @Test
    void testUpdateModificationRestriction() throws Exception {
        User updated = getUpdated();
        TestUtil.print(
                mockMvc.perform(
                        put(REST_URL + USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeJsonWithPassword(updated))
                                .with(userHttpBasic(ADMIN)))
                        .andDo(print()))
                .andExpect(status().isUnavailableForLegalReasons())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonErrorType(APP_ERROR))
                .andExpect(jsonErrorDetails(EXCEPTION_MODIFICATION_RESTRICTION, USER.getName()));

        assertMatch(userService.get(USER_ID), USER);
    }

}