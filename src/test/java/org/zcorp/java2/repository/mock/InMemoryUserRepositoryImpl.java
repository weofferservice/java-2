package org.zcorp.java2.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.UserTestData.*;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(START_SEQ + 1);

    public void init() {
        repository.clear();
        repository.put(USER_ID, USER);
        repository.put(ADMIN_ID, ADMIN);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream()
                .filter(user -> Objects.equals(email, user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}