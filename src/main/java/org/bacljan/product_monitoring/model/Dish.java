package org.bacljan.product_monitoring.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishIngredient> dishIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "dish")
    private List<OrderItem> items = new ArrayList<>();

    public void addIngredient(DishIngredient ingredient) {
        dishIngredients.add(ingredient);
        ingredient.setDish(this);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setDish(this);
    }
}
