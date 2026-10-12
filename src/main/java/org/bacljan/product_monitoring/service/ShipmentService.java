package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.exception.ShipmentAlreadyExistsException;
import org.bacljan.product_monitoring.model.Shipment;
import org.bacljan.product_monitoring.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;

    }
    @Transactional
    public Shipment addShipment(Shipment shipment){
        if(shipment == null)
            throw new NullPointerException("Shipment cannot be null");
        return shipmentRepository.save(shipment);
    }



}
