package org.zcorp.java2.repository.mock;

import org.slf4j.Logger;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MockUserRepositoryImpl implements UserRepository {
    private static final Logger log = getLogger(MockUserRepositoryImpl.class);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return true;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return null;
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return Collections.emptyList();
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return null;
    }
}
