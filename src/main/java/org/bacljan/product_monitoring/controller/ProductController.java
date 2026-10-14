package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.DtoMapper;
import org.bacljan.product_monitoring.DTO.ProductInfoDto;
import org.bacljan.product_monitoring.DTO.ProductInfoResponseDto;
import org.bacljan.product_monitoring.service.ProductInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductInfoService productService;

    public ProductController(ProductInfoService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductInfoResponseDto>> getProducts() {
        return ResponseEntity.ok(DtoMapper.toProductInfoDtoList(productService.getAll()));
    }

    @PostMapping
    public ResponseEntity<List<ProductInfoResponseDto>> registerProducts(
            @Valid @RequestBody List<ProductInfoDto> productDtos) {
        return ResponseEntity.ok(DtoMapper.toProductInfoDtoList(productService.add(productDtos)));
    }
}
