package org.zcorp.java2.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zcorp.java2.model.User;
import org.zcorp.java2.service.UserService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.ValidationUtil.assureIdConsistent;
import static org.zcorp.java2.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info((enabled ? "enable" : "disable") + " user with id={}", id);
        service.enable(id, enabled);
    }
}