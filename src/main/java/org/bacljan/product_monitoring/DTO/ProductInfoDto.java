package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class ProductInfoDto {
    @NotBlank
    String title;
    @NotBlank
    String unit;
}
