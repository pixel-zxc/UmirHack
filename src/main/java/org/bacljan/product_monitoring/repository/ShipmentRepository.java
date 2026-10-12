package org.bacljan.product_monitoring.repository;

import org.bacljan.product_monitoring.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    boolean existsByShipmentNumber(String shipmentNumber);
}
