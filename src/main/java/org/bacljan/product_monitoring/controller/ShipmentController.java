package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.model.Shipment;
import org.bacljan.product_monitoring.service.ProductService;
import org.bacljan.product_monitoring.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ProductService productService;

    public ShipmentController(ShipmentService shipmentService, ProductService productService) {
        this.shipmentService = shipmentService;
        this.productService = productService;
    }

    @PostMapping("/shipment")
    public ResponseEntity<Shipment> addShipment(@RequestBody @Valid ShipmentDto dto) {
        Shipment shipment = shipmentService.add(dto);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/stocks")
    public ResponseEntity<Map<Long, Integer>> getStocks() {
        return ResponseEntity.ok(productService.getStocks());
    }

    @GetMapping("/stocks/batches")
    public ResponseEntity<List<Product>> getAllBatches() {
        return ResponseEntity.ok(productService.getAllBatches());
    }

    @GetMapping("/stocks/batches/{id}")
    public ResponseEntity<List<Product>> getBatchesByTitle(@PathVariable long id) {
        return ResponseEntity.ok(productService.getBatchesByProductId(id));
    }
}