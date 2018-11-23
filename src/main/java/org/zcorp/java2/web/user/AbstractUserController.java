package org.zcorp.java2.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.zcorp.java2.Profiles;
import org.zcorp.java2.model.AbstractBaseEntity;
import org.zcorp.java2.model.User;
import org.zcorp.java2.service.UserService;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.exception.ModificationRestrictionException;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.ValidationUtil.assureIdConsistent;
import static org.zcorp.java2.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    private UserService service;

    private boolean modificationRestriction;

    @Autowired
    public void setEnvironment(Environment environment) {
        modificationRestriction = environment.acceptsProfiles(Profiles.HEROKU);
    }

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
        checkModificationAllowed(id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        checkModificationAllowed(id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        checkModificationAllowed(id);
        service.update(userTo);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info((enabled ? "enable" : "disable") + " user with id={}", id);
        checkModificationAllowed(id);
        service.enable(id, enabled);
    }

    private void checkModificationAllowed(int id) {
        if (modificationRestriction && id < AbstractBaseEntity.START_SEQ + 2) {
            User user = get(id);
            throw new ModificationRestrictionException(user.getName());
        }
    }
}