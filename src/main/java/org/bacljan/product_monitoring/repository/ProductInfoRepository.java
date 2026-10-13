package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
    Optional<ProductInfo> findByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCase(String title);
}
