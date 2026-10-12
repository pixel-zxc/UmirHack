package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class ProductDto {
    @NotBlank
    String title;
    @Min(1)
    int quantity;
    @Future
    Instant expirationDate;
    @NotBlank
    String unit;
}
