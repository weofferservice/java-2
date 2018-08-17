package org.zcorp.java2.web;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.mock.InMemoryUserRepositoryImpl;
import org.zcorp.java2.util.exception.NotFoundException;
import org.zcorp.java2.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Collection;

import static org.zcorp.java2.UserTestData.ADMIN;

public class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-mock.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller = appCtx.getBean(AdminRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() throws Exception {
        // re-initialize
        InMemoryUserRepositoryImpl repository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
        repository.init();
    }

    @Test
    public void delete() throws Exception {
        controller.delete(UserTestData.USER_ID);
        Collection<User> users = controller.getAll();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(ADMIN, users.iterator().next());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        controller.delete(10);
    }
}