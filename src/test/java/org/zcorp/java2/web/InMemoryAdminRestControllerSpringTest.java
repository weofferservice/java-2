package org.zcorp.java2.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zcorp.java2.UserTestData;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.mock.InMemoryUserRepositoryImpl;
import org.zcorp.java2.util.exception.NotFoundException;
import org.zcorp.java2.web.user.AdminRestController;

import java.util.Collection;

import static org.zcorp.java2.UserTestData.ADMIN;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-mock.xml"})
@RunWith(SpringRunner.class)
public class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
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
