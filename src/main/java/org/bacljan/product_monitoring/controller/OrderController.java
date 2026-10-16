package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.DtoMapper;
import org.bacljan.product_monitoring.DTO.OrderDto;
import org.bacljan.product_monitoring.DTO.OrderResponseDto;
import org.bacljan.product_monitoring.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> list(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(status).stream().map(DtoMapper::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(DtoMapper.toDto(service.createOrder(dto)));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<OrderResponseDto> complete(@PathVariable long id) {
        return ResponseEntity.ok(DtoMapper.toDto(service.complete(id)));
    }
}
