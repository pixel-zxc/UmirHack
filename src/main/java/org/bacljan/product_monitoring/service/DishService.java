package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.DishDto;
import org.bacljan.product_monitoring.DTO.DishIngredientDto;
import org.bacljan.product_monitoring.exception.DishNotFoundException;
import org.bacljan.product_monitoring.model.Dish;
import org.bacljan.product_monitoring.model.DishIngredient;
import org.bacljan.product_monitoring.repository.DishRepository;
import org.bacljan.product_monitoring.repository.ProductInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final ProductInfoRepository infoRepository;

    @Transactional
    public Dish createDish(DishDto dto) {
        if (dto == null) throw new NullPointerException("Dish dto cannot be a null");
        Dish dish = new Dish();
        dish.setTitle(dto.getTitle());
        dish.setDishIngredients(new ArrayList<>());
        dish.setItems(new ArrayList<>());

        for (DishIngredientDto ingredientDto : dto.getIngredient()) {
            DishIngredient ingredient = new DishIngredient();
            ingredient.setProductInfo(infoRepository.findById(
                    ingredientDto.getIdProduct()
            ).orElseThrow());
            ingredient.setQuantity(ingredientDto.getQuantity());
            dish.addIngredient(ingredient);
        }

        return dishRepository.save(dish);
    }

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public Dish findById(long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("dish not found"));
    }
}
