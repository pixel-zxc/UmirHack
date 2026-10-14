package org.bacljan.product_monitoring.mapper;

import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.model.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Shipment toEntity(ShipmentDto dto);
}
