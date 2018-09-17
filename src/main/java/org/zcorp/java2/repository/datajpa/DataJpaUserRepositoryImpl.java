package org.zcorp.java2.repository.datajpa;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import java.util.List;

@Repository
public class DataJpaUserRepositoryImpl implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");

    @Autowired
    private CrudUserRepository crudRepository;

    @Override
    public User save(User user) {
        if (user.isNew()) {
            return crudRepository.save(user);
        } else {
            if (crudRepository.update(
                    user.getId(), user.getName(), user.getEmail(), user.getPassword(),
                    user.isEnabled(), user.getRegistered(), user.getCaloriesPerDay()) == 0) {
                return null;
            }
            return user;
        }
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User getWithMeals(int id) {
        User user = get(id);
        if (user != null) {
            Hibernate.initialize(user.getMeals());
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }
}
