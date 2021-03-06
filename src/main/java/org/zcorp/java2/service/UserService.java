package org.zcorp.java2.service;

import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.exception.NotFoundException;

import java.util.List;

public interface UserService {

    User create(User user);

    void delete(int id) throws NotFoundException;

    User get(int id) throws NotFoundException;

    User getWithMeals(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    void update(User userFrom) throws NotFoundException;

    void update(UserTo user) throws NotFoundException;

    List<User> getAll();

    void enable(int id, boolean enabled) throws NotFoundException;

}