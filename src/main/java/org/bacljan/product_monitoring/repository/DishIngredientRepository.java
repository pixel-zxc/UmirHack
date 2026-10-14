package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishIngredientRepository extends JpaRepository<DishIngredient, Long> {
}
