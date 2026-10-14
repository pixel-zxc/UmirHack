package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.exception.ShipmentAlreadyExistsException;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.model.ProductInfo;
import org.bacljan.product_monitoring.model.Shipment;
import org.bacljan.product_monitoring.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ProductInfoService productInfoService;

    @Transactional
    public Shipment add(ShipmentDto dto) {
        if (shipmentRepository.existsByShipmentNumber(dto.getShipmentNumber())) {
            throw new ShipmentAlreadyExistsException("Shipment already exists");
        }

        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(dto.getShipmentNumber());
        shipment.setDateShipment(dto.getDateShipment());

        for (ProductDto b : dto.getProducts()) {
            ProductInfo info = productInfoService.getById(b.getProductId());

            Product product = new Product();
            product.setProductInfo(info);
            product.setQuantity(b.getQuantity());
            product.setExpirationDate(b.getExpirationDate());

            shipment.addProduct(product);
        }

        return shipmentRepository.save(shipment);
    }
}