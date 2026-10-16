package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.productInfo LEFT JOIN FETCH p.shipment WHERE p.quantity > 0 ORDER BY p.expirationDate ASC")
    List<Product> findActiveBatches();

    @Query("SELECT p FROM Product p JOIN FETCH p.productInfo LEFT JOIN FETCH p.shipment WHERE p.productInfo.id = :productInfoId AND p.quantity > 0 ORDER BY p.expirationDate ASC")
    List<Product> findActiveBatchesByProductInfoId(@Param("productInfoId") Long productInfoId);
}
