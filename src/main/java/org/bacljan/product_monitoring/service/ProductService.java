package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.mapper.ProductMapper;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Map<String, Integer> getStocks() {
        return repository.findAll().stream()
                .filter(p -> p.getQuantity() > 0)
                .collect(Collectors.groupingBy(
                        Product::getTitle,
                        Collectors.summingInt(Product::getQuantity)
                ));
    }

    public List<Product> getBatchesSortedByExpiry() {
        return repository.findAll().stream()
                .filter(p -> p.getQuantity() > 0)
                .sorted(Comparator.comparing(Product::getExpirationDate))
                .toList();
    }

    public List<Product> getBatchesByTitle(String title) {
        return repository.findByTitleIgnoreCase(title).stream()
                .filter(p -> p.getQuantity() > 0)
                .sorted(Comparator.comparing(Product::getExpirationDate))
                .toList();
    }

    @Transactional
    public List<Product> addProducts(List<ProductDto> products) {
        return repository.saveAll(mapper.toListEntity(products));
    }
}