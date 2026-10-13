package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.ProductInfoDto;
import org.bacljan.product_monitoring.model.ProductInfo;
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
    public ResponseEntity<List<ProductInfo>> getProducts(){
        return ResponseEntity.ok(productService.getAll());
    }
    @PostMapping
    public ResponseEntity<List<ProductInfo>> registerProducts(@Valid @RequestBody List<ProductInfoDto> productDtos){
        List<ProductInfo> savedProducts =  productService.add(productDtos);
        return ResponseEntity.ok(savedProducts);
    }

}
