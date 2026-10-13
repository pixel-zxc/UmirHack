package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(Long productInfoId, int quantity);
    List<Product> findByQuantityGreaterThanOrderByExpirationDateAsc(int quantity);
}
