package org.zcorp.java2.repository;

import org.zcorp.java2.model.User;

import java.util.List;

public interface UserRepository {
    // null if update and not found
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    default User getWithMeals(int id) {
        throw new UnsupportedOperationException();
    }

    // null if not found
    User getByEmail(String email);

    List<User> getAll();
}