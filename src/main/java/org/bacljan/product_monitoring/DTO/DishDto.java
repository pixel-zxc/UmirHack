package org.bacljan.product_monitoring.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.List;

@Value
public class DishDto {
    @NotBlank
    String title;
    /** optional: http(s) URL or data:image base64 */
    String imageUrl;
    @NotNull
    @Size(min = 1)
    List<DishIngredientDto> ingredient;
}
