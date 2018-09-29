package org.zcorp.java2.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.mock.InMemoryUserRepositoryImpl;
import org.zcorp.java2.util.exception.NotFoundException;
import org.zcorp.java2.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zcorp.java2.UserTestData.ADMIN;

public class InMemoryAdminRestControllerTest {
    private static ConfigurableWebApplicationContext appCtx;
    private static AdminRestController controller;

    @BeforeAll
    public static void beforeClass() {
        ApplicationContext parentAppCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-mock.xml");
        appCtx = new XmlWebApplicationContext();
        appCtx.setParent(parentAppCtx);
        appCtx.setConfigLocation("spring/spring-mvc.xml");
        appCtx.setServletContext(new MockServletContext());
        appCtx.refresh();
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller = appCtx.getBean(AdminRestController.class);
    }

    @AfterAll
    public static void afterClass() {
        //May cause during JUnit "Cache is not alive (STATUS_SHUTDOWN)" as JUnit share Spring context for speed
        //https://stackoverflow.com/questions/16281802/ehcache-shutdown-causing-an-exception-while-running-test-suite
        //If some tests will fail then you need to comment out lines below
        appCtx.close();
        ((AbstractApplicationContext) appCtx.getParent()).close();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // re-initialize
        InMemoryUserRepositoryImpl repository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
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