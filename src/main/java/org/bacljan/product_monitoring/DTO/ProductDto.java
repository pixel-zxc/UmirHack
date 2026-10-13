package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class ProductDto {
    @NotNull
    Long productId;
    @Min(1)
    int quantity;
    @NotNull
    @Future
    Instant expirationDate;
}
