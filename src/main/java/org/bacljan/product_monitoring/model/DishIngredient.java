package org.bacljan.product_monitoring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "dish_ingredients")
public class DishIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;
    @ManyToOne
    @JoinColumn(name = "product_info_id")
    private ProductInfo productInfo;
    private int quantity;
}

