package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public class OrderItemDto {
    long dishId;
    @Min(1)
    int quantity;
}
