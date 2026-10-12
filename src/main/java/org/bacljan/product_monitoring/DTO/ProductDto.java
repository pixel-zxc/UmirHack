package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.time.Instant;

@Value
public class ProductDto {
    @NotBlank
    private String title;
    @Min(1)
    private int quantity;
    @Future
    private Instant expirationDate;
    @NotBlank
    private String unit;
}
