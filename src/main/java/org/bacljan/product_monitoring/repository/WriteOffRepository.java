package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {
    @Query("select sum(w.quantity) from WriteOff w")
    Long sumAllQuantity();

    @Query("select w.productInfo.id, sum(w.quantity) from WriteOff w group by w.productInfo.id order by sum(w.quantity) desc")
    List<Object[]> sumByProductInfo();
}
