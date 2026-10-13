package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Map<Long, Integer> getStocks() {
        return repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0)
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getProductInfo().getId(),
                        Collectors.summingInt(Product::getQuantity)
                ));
    }

    public List<Product> getAllBatches() {
        return repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0);
    }

    public List<Product> getBatchesByProductId(Long productId) {
        return repository.findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(productId, 0);
    }
}