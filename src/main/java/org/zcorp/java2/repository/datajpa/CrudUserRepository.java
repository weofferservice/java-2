package org.zcorp.java2.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Override
    @Transactional
    User save(User user);

    @Override
    Optional<User> findById(Integer id);

    @Override
    List<User> findAll(Sort sort);

    User getByEmail(String email);

    // Применяется в DataJpaMealRepositoryImpl для получения прокси на User-а
    @Override
    User getOne(Integer id);

//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.id=?1")
    @Query("SELECT u FROM User u WHERE u.id=?1")
//    @EntityGraph(User.GRAPH_WITH_MEALS)
    @EntityGraph(attributePaths = {"meals"})
    User getWithMeals(int id);
}
