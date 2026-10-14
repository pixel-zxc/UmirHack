package org.bacljan.product_monitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "products_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Setter
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "units")
    private String unit;
    @Column(name = "title", nullable = false)
    private String title;
    @OneToMany(mappedBy = "productInfo")
    private List<DishIngredient> ingredients = new ArrayList<>();

    public void add(DishIngredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setProductInfo(this);
    }
}
