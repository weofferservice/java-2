package org.zcorp.java2.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(name = User.UPDATE)
//    @Query("UPDATE User u SET u.name=:name, u.email=:email, u.password=:password, u.enabled=:enabled, u.registered=:registered, u.caloriesPerDay=:caloriesPerDay WHERE u.id=:id")
    int update(@Param("id") Integer id, @Param("name") String name, @Param("email") String email, @Param("password") String password,
               @Param("enabled") boolean enabled, @Param("registered") Date registered, @Param("caloriesPerDay") int caloriesPerDay);

    @Override
    @Transactional
    User save(User user);

    @Override
    Optional<User> findById(Integer id);

    @Override
    List<User> findAll(Sort sort);

    User getByEmail(String email);
}
