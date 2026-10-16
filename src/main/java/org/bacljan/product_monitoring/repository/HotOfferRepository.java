package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.HotOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotOfferRepository extends JpaRepository<HotOffer, Long> {

    @Query("SELECT h FROM HotOffer h JOIN FETCH h.dish WHERE h.active = true ORDER BY h.priority DESC")
    List<HotOffer> findActiveWithDish();
}
