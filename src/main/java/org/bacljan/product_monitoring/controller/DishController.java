package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.DishDto;
import org.bacljan.product_monitoring.DTO.DishResponseDto;
import org.bacljan.product_monitoring.DTO.DtoMapper;
import org.bacljan.product_monitoring.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@AllArgsConstructor
public class DishController {
    private final DishService service;

    @GetMapping
    public ResponseEntity<List<DishResponseDto>> getDishes() {
        return ResponseEntity.ok(DtoMapper.toDishDtoList(service.getDishes()));
    }

    @PostMapping
    public ResponseEntity<DishResponseDto> createDish(@Valid @RequestBody DishDto dto) {
        return ResponseEntity.ok(DtoMapper.toDto(service.createDish(dto)));
    }
}
