package org.zcorp.java2.service;

import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;
import org.zcorp.java2.util.exception.NotFoundException;

import java.util.List;

import static org.zcorp.java2.util.ValidationUtil.checkNotFound;
import static org.zcorp.java2.util.ValidationUtil.checkNotFoundWithId;

public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public void update(User user) throws NotFoundException {
        checkNotFoundWithId(repository.save(user), user.getId());
    }

}