package org.bacljan.product_monitoring.mapper;

import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.model.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto dto);

    List<Product> toListEntity(List<ProductDto> dtos);
    List<ProductDto> toListDtos(List<Product> products);
}
