package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SeedResultDto {
    int productsCreated;
    int shipmentsCreated;
    int dishesCreated;
    String message;
}
