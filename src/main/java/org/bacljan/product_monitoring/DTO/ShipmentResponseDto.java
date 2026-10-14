package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class ShipmentResponseDto {
    long id;
    String shipmentNumber;
    Instant dateShipment;
    List<ProductBatchResponseDto> products;
}
