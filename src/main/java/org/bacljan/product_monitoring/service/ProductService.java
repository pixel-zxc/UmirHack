package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.exception.IllegalProductQuantityException;
import org.bacljan.product_monitoring.exception.ProductIsExirationException;
import org.bacljan.product_monitoring.mapper.ProductMapper;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public List<Product> getProducts(){
        return repository.findAll();
    }
    @Transactional
    public List<Product> addProducts(List<ProductDto> products){

        return repository.saveAll(mapper.toListEntity(products));
    }


}
