package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.DishDto;
import org.bacljan.product_monitoring.DTO.DishIngredientDto;
import org.bacljan.product_monitoring.exception.DishNotFoundException;
import org.bacljan.product_monitoring.model.Dish;
import org.bacljan.product_monitoring.model.DishIngredient;
import org.bacljan.product_monitoring.repository.DishRepository;
import org.bacljan.product_monitoring.repository.ProductInfoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final ProductInfoRepository infoRepository;
    private final AppCacheService appCacheService;

    @Transactional
    public Dish createDish(DishDto dto) {
        if (dto == null) throw new NullPointerException("Dish dto cannot be a null");
        Dish dish = new Dish();
        dish.setTitle(dto.getTitle());
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            dish.setImageUrl(dto.getImageUrl().trim());
        }
        dish.setDishIngredients(new ArrayList<>());
        dish.setItems(new ArrayList<>());

        for (DishIngredientDto ingredientDto : dto.getIngredient()) {
            DishIngredient ingredient = new DishIngredient();
            ingredient.setProductInfo(infoRepository.findById(
                    ingredientDto.getIdProduct()
            ).orElseThrow(() -> new DishNotFoundException("product not found")));
            ingredient.setQuantity(ingredientDto.getQuantity());
            dish.addIngredient(ingredient);
        }

        Dish saved = dishRepository.save(dish);
        appCacheService.evictDishes();
        return saved;
    }

    @Transactional(readOnly = true)
    @Cacheable("dishes")
    public List<Dish> getDishes() {
        List<Dish> dishes = dishRepository.findAll();
        dishes.forEach(d -> {
            if (d.getDishIngredients() != null) {
                d.getDishIngredients().size();
            }
        });
        return dishes;
    }

    @Transactional(readOnly = true)
    public Dish findById(long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("dish not found"));
        if (dish.getDishIngredients() != null) {
            dish.getDishIngredients().size();
        }
        return dish;
    }
}
