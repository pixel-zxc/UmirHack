package org.bacljan.product_monitoring.controller;

import jakarta.annotation.Nonnull;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.service.ProductService;
import org.bacljan.product_monitoring.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final ProductService productService;
    public ShipmentController(ShipmentService service,ProductService productService) {
        this.shipmentService = service;
        this.productService = productService;
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.of(Optional.ofNullable(productService.getProducts()));
    }
    @PostMapping("/products")
    public ResponseEntity<Product> registerProducts(@Nonnull){

    }

}
