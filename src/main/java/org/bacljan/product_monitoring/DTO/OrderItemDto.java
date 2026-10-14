package org.bacljan.product_monitoring.DTO;

import lombok.Value;

@Value
public class OrderItemDto {
    long dishId;
    int quantity;
}
