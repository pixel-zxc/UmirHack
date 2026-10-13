package org.bacljan.product_monitoring.service;

import org.bacljan.product_monitoring.DTO.ProductInfoDto;
import org.bacljan.product_monitoring.model.ProductInfo;
import org.bacljan.product_monitoring.repository.ProductInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductInfoService {

    private final ProductInfoRepository repository;

    public ProductInfoService(ProductInfoRepository repository) {
        this.repository = repository;
    }

    public List<ProductInfo> getAll() {
        return repository.findAll();
    }

    @Transactional
    public List<ProductInfo> add(List<ProductInfoDto> dtos) {
        List<ProductInfo> list = dtos.stream().map(dto -> {
            ProductInfo p = new ProductInfo();
            p.setTitle(dto.getTitle());
            p.setUnit(dto.getUnit());
            return p;
        }).toList();
        return repository.saveAll(list);
    }

    public ProductInfo getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }
}