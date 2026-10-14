package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.OrderDto;
import org.bacljan.product_monitoring.model.Order;
import org.bacljan.product_monitoring.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(service.createOrder(dto));
    }
}
