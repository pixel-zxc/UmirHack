package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.bacljan.product_monitoring.model.OrderStatus;

import java.time.Instant;
import java.util.List;

@Value
@AllArgsConstructor
public class OrderResponseDto {
    long id;
    Instant createdAt;
    OrderStatus status;
    List<OrderItemResponseDto> items;
}
