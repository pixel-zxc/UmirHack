package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.*;
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
    public ResponseEntity<ShipmentResponseDto> addShipment(@RequestBody @Valid ShipmentDto dto) {
        return ResponseEntity.ok(DtoMapper.toDto(shipmentService.add(dto)));
    }

    @GetMapping("/stocks")
    public ResponseEntity<List<StockItemDto>> getStocks() {
        return ResponseEntity.ok(productService.getStockItems());
    }

    @GetMapping("/stocks/batches")
    public ResponseEntity<List<ProductBatchResponseDto>> getAllBatches() {
        return ResponseEntity.ok(DtoMapper.toProductBatchDtoList(productService.getAllBatches()));
    }

    @GetMapping("/stocks/batches/{id}")
    public ResponseEntity<List<ProductBatchResponseDto>> getBatchesByProductId(@PathVariable long id) {
        return ResponseEntity.ok(DtoMapper.toProductBatchDtoList(productService.getBatchesByProductId(id)));
    }

    @GetMapping("/stoplist")
    public ResponseEntity<List<DishResponseDto>> getStopList() {
        return ResponseEntity.ok(DtoMapper.toDishDtoList(productService.getStopList()));
    }

    @GetMapping("/stoplist/forecast")
    public ResponseEntity<List<StopListForecastDto>> getStopListForecast() {
        return ResponseEntity.ok(productService.getStopListForecast());
    }

    @GetMapping("/stocks/expiring")
    public ResponseEntity<List<ProductBatchResponseDto>> getExpiring(@RequestParam(defaultValue = "3") int days) {
        return ResponseEntity.ok(DtoMapper.toProductBatchDtoList(productService.getExpiringBatches(days)));
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

    @PostMapping("/hot-offers")
    public ResponseEntity<HotOfferDto> createHotOffer(@RequestBody @Valid HotOfferCreateDto dto) {
        return ResponseEntity.ok(productService.createHotOffer(dto));
    }

    @DeleteMapping("/hot-offers/{id}")
    public ResponseEntity<Void> deleteHotOffer(@PathVariable long id) {
        productService.deleteHotOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(productService.getAnalytics());
    }
}
