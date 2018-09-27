package org.zcorp.java2.repository.jpa;

import org.hibernate.jpa.QueryHints;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.User;
import org.zcorp.java2.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaUserRepositoryImpl implements UserRepository {

/*
    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }
*/

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
        } else if (em.createNamedQuery(User.UPDATE)
                .setParameter("id", user.getId())
                .setParameter("name", user.getName())
                .setParameter("email", user.getEmail())
                .setParameter("password", user.getPassword())
                .setParameter("enabled", user.isEnabled())
                .setParameter("registered", user.getRegistered())
                .setParameter("caloriesPerDay", user.getCaloriesPerDay())
                .executeUpdate() == 0) {
            return null;
        } else {
            Query deleteRoles = em.createNativeQuery("DELETE FROM user_roles r WHERE r.user_id=?1");
            deleteRoles.setParameter(1, user.getId());
            deleteRoles.executeUpdate();

            Query insertRoles = em.createNativeQuery("INSERT INTO user_roles (user_id, role) VALUES (:id, :role)");
            user.getRoles().forEach(role -> {
                insertRoles.setParameter("id", user.getId())
                        .setParameter("role", role.name())
                        .executeUpdate();
            });
        }
        return user;
    }

    @Override
    public User get(int id) {
        return em.find(User.class, id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
//        User ref = em.getReference(User.class, id);
//        em.remove(ref);

//        Query query = em.createQuery("DELETE FROM User u WHERE u.id=:id");
//        return query.setParameter("id", id).executeUpdate() != 0;

        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = em.createNamedQuery(User.BY_EMAIL, User.class)
                .setParameter(1, email)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.ALL_SORTED, User.class).getResultList();
    }

}
