package org.zcorp.java2.web.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.UserUtil;

import java.util.List;

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminAjaxController extends AbstractUserController {

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    public void createOrUpdate(UserTo userTo) {
        if (userTo.isNew()) {
            super.create(UserUtil.createFromTo(userTo));
        } else {
            super.update(userTo, userTo.getId());
        }
    }

    @Override
    @PostMapping("/{id}")
    public void enable(@PathVariable("id") int id, @RequestParam("enabled") boolean enabled) {
        super.enable(id, enabled);
    }

}