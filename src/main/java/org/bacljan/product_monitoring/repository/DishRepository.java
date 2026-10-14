package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}

