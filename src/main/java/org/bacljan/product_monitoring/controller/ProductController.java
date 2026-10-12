package org.bacljan.product_monitoring.controller;

import jakarta.validation.Valid;
import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }
    @PostMapping
    public ResponseEntity<List<Product>> registerProducts(@Valid @RequestBody List<ProductDto> productDtos){
        List<Product> savedProducts =  productService.addProducts(productDtos);
        return ResponseEntity.ok(savedProducts);
    }

}
