package org.zcorp.java2.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zcorp.java2.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Override
    @Transactional
    Meal save(Meal meal);

    @Override
    Optional<Meal> findById(Integer id);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId")
    List<Meal> getAll(@Param("userId") int userId, Sort sort);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDateTime AND :endDateTime")
    List<Meal> getBetween(@Param("startDateTime") LocalDateTime startDateTime,
                          @Param("endDateTime") LocalDateTime endDateTime,
                          @Param("userId") int userId,
                          Sort sort);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=?1 and m.user.id=?2")
    Meal getWithUser(int id, int userId);
}
