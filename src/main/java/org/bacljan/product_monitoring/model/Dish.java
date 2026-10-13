package org.bacljan.product_monitoring.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY)
    private List<DishIngredient> dishIngredients;
    public void add(DishIngredient ingredient){
        dishIngredients.add(ingredient);
        ingredient.setDish(this);
    }
}
