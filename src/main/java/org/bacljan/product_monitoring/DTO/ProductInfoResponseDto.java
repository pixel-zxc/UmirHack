package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ProductInfoResponseDto {
    Long id;
    String title;
    String unit;
}
