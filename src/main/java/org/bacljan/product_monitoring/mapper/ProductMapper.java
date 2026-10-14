package org.bacljan.product_monitoring.mapper;

import org.bacljan.product_monitoring.DTO.ProductDto;
import org.bacljan.product_monitoring.DTO.ProductInfoDto;
import org.bacljan.product_monitoring.model.Product;
import org.bacljan.product_monitoring.model.ProductInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductInfo toEntity(ProductInfoDto dto);

    ProductInfoDto toDto(ProductInfo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "productInfo", ignore = true)
    Product toEntity(ProductDto dto);
}
