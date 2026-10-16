package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HotOfferCreateDto {
    @NotNull
    private Long dishId;

    @Positive
    private double priority = 1.0;

    private String reason;
}
