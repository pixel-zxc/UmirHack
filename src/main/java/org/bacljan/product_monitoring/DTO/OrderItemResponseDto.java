package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class OrderItemResponseDto {
    long id;
    long dishId;
    String dishTitle;
    int quantity;
}
