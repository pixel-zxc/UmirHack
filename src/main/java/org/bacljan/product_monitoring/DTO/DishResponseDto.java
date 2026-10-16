package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class DishResponseDto {
    long id;
    String title;
    String imageUrl;
    List<DishIngredientResponseDto> ingredients;
}
