package org.zcorp.java2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.zcorp.java2.model.Role;
import org.zcorp.java2.model.User;
import org.zcorp.java2.util.exception.NotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.zcorp.java2.UserTestData.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(service.getAll(), ADMIN, newUser, USER);
    }

    @Test
    public void createDuplicateMail() throws Exception {
        assertThrows(
                DataAccessException.class,
                () -> service.create(
                        new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER)
                )
        );
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_ID);
        assertMatch(service.getAll(), ADMIN);
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(
                NotFoundException.class,
                () -> service.delete(1));
    }

    @Test
    public void get() throws Exception {
        User user = service.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(
                NotFoundException.class,
                () -> service.get(1));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        assertMatch(user, ADMIN);
    }

    @Test
    public void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setRoles(EnumSet.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        service.update(new User(updated));
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        User updated = new User(USER);
        updated.setId(1);
        assertThrows(
                NotFoundException.class,
                () -> service.update(updated)
        );
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER);
    }

    @Test
    public void getWithMeals() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> service.getWithMeals(USER_ID)
        );
    }

    @Test
    public void enable() {
        service.enable(USER_ID, false);
        assertFalse(service.get(USER_ID).isEnabled());
        service.enable(USER_ID, true);
        assertTrue(service.get(USER_ID).isEnabled());
    }

    @Test
    public void enableNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> service.enable(1, false)
        );
    }

}