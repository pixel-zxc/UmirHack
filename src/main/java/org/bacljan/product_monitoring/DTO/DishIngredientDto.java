package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public class DishIngredientDto {
    @Min(1)
    long idProduct;
    @Min(1)
    int quantity;
}
