package org.bacljan.product_monitoring.DTO;

import lombok.Value;

import java.util.List;

@Value
public class OrderDto {
    List<OrderItemDto> dtos;
}
