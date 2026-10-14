package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class ProductBatchResponseDto {
    long id;
    Long productInfoId;
    String productTitle;
    int quantity;
    Instant expirationDate;
    Long shipmentId;
}
