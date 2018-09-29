package org.zcorp.java2.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.mock.InMemoryUserRepositoryImpl;
import org.zcorp.java2.util.exception.NotFoundException;
import org.zcorp.java2.web.user.AdminRestController;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zcorp.java2.UserTestData.ADMIN;

//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@ContextConfiguration
@SpringJUnitWebConfig(locations = {"classpath:spring/spring-mvc.xml", "classpath:spring/spring-app.xml", "classpath:spring/spring-mock.xml"})
public class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepositoryImpl repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository.init();
    }

    @Test
    public void delete() throws Exception {
        controller.delete(UserTestData.USER_ID);
        Collection<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.iterator().next());
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(
                NotFoundException.class,
                () -> controller.delete(10));
    }

}
