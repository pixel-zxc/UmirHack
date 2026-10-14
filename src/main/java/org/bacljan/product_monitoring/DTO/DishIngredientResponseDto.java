package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DishIngredientResponseDto {
    Long productInfoId;
    String productTitle;
    int quantity;
}
