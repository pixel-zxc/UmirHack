package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.AnalyticsDto;
import org.bacljan.product_monitoring.DTO.HotOfferDto;
import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.model.Dish;
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

    @GetMapping("/stoplist")
    public ResponseEntity<List<Dish>> getStopList() {
        return ResponseEntity.ok(productService.getStopList());
    }

    @GetMapping("/stocks/expiring")
    public ResponseEntity<List<Product>> getExpiring(@RequestParam(defaultValue = "3") int days) {
        return ResponseEntity.ok(productService.getExpiringBatches(days));
    }

    @PostMapping("/stocks/write-off-expired")
    public ResponseEntity<Map<String, Integer>> writeOffExpired() {
        int written = productService.writeOffExpired();
        return ResponseEntity.ok(Map.of("writtenOff", written));
    }

    @GetMapping("/hot-offers")
    public ResponseEntity<List<HotOfferDto>> getHotOffers() {
        return ResponseEntity.ok(productService.getHotOffers());
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(productService.getAnalytics());
    }
}
