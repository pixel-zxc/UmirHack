package org.bacljan.product_monitoring.mapper;

import org.bacljan.product_monitoring.DTO.ShipmentDto;
import org.bacljan.product_monitoring.model.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface ShipmentMapper {
    ShipmentDto toDto(Shipment entity);
    Shipment toEntity(ShipmentDto dto);


}
