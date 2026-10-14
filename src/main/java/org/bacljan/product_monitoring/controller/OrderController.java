package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.DtoMapper;
import org.bacljan.product_monitoring.DTO.OrderDto;
import org.bacljan.product_monitoring.DTO.OrderResponseDto;
import org.bacljan.product_monitoring.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(DtoMapper.toDto(service.createOrder(dto)));
    }
}
