package org.bacljan.product_monitoring.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.util.List;

@Value
public class OrderDto {
    @NotEmpty
    @Valid
    List<OrderItemDto> dtos;
}
