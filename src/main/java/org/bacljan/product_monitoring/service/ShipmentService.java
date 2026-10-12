package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.exception.IllegalProductQuantityException;
import org.bacljan.product_monitoring.exception.ProductIsExirationException;
import org.bacljan.product_monitoring.exception.ShipmentAlreadyExistsException;
import org.bacljan.product_monitoring.mapper.ProductMapper;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.model.Shipment;
import org.bacljan.product_monitoring.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ProductMapper productMapper;

    public ShipmentService(ShipmentRepository shipmentRepository,
                           ProductMapper productMapper) {
        this.shipmentRepository = shipmentRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public Shipment addShipment(ShipmentDto dto) {
        if (shipmentRepository.existsByShipmentNumber(dto.getShipmentNumber())) {
            throw new ShipmentAlreadyExistsException(
                    "Поставка с номером " + dto.getShipmentNumber() + " уже существует");
        }

        Shipment shipment = new Shipment();
        shipment.setShipmentNumber(dto.getShipmentNumber());
        shipment.setDateShipment(dto.getDateShipment());

        for (ProductDto productDto : dto.getProducts()) {
            validateProduct(productDto);

            Product product = productMapper.toEntity(productDto);
            shipment.addProduct(product);
        }

        return shipmentRepository.save(shipment);
    }

    private void validateProduct(ProductDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new IllegalProductQuantityException(
                    "Количество продукта \"" + dto.getTitle() + "\" должно быть больше 0");
        }
        if (dto.getExpirationDate().isBefore(Instant.now())) {
            throw new ProductIsExirationException(
                    "Срок годности продукта \"" + dto.getTitle() + "\" уже истёк");
        }
    }
}