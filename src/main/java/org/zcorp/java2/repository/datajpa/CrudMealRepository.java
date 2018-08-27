package org.zcorp.java2.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zcorp.java2.model.Meal;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
}
