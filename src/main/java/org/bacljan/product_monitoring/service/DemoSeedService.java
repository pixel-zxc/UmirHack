package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.SeedResultDto;
import org.bacljan.product_monitoring.model.*;
import org.bacljan.product_monitoring.repository.DishRepository;
import org.bacljan.product_monitoring.repository.ProductInfoRepository;
import org.bacljan.product_monitoring.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DemoSeedService {
    private final ProductInfoRepository productInfoRepository;
    private final ShipmentRepository shipmentRepository;
    private final DishRepository dishRepository;
    private final StockEventService stockEventService;
    private final AppCacheService appCacheService;

    @Transactional
    public SeedResultDto seed() {
        if (productInfoRepository.count() > 0) {
            return new SeedResultDto(0, 0, 0, "already seeded");
        }

        ProductInfo chicken = info("Chicken", "kg");
        ProductInfo rice = info("Rice", "kg");
        ProductInfo tomato = info("Tomato", "kg");
        ProductInfo cheese = info("Cheese", "kg");
        ProductInfo milk = info("Milk", "l");
        productInfoRepository.saveAll(List.of(chicken, rice, tomato, cheese, milk));

        Instant now = Instant.now();

        Shipment s1 = shipment("SHIP-001", now.minus(2, ChronoUnit.DAYS));
        s1.addProduct(batch(chicken, 5, now.plus(1, ChronoUnit.DAYS)));
        s1.addProduct(batch(rice, 20, now.plus(30, ChronoUnit.DAYS)));
        s1.addProduct(batch(tomato, 8, now.plus(2, ChronoUnit.DAYS)));
        shipmentRepository.save(s1);

        Shipment s2 = shipment("SHIP-002", now.minus(1, ChronoUnit.DAYS));
        s2.addProduct(batch(chicken, 10, now.plus(5, ChronoUnit.DAYS)));
        s2.addProduct(batch(cheese, 4, now.plus(3, ChronoUnit.DAYS)));
        s2.addProduct(batch(milk, 6, now.plus(1, ChronoUnit.DAYS)));
        shipmentRepository.save(s2);

        Shipment s3 = shipment("SHIP-003", now);
        s3.addProduct(batch(tomato, 12, now.plus(7, ChronoUnit.DAYS)));
        s3.addProduct(batch(rice, 15, now.plus(40, ChronoUnit.DAYS)));
        shipmentRepository.save(s3);

        Dish salad = dish("Caesar Salad");
        salad.addIngredient(ingredient(chicken, 1));
        salad.addIngredient(ingredient(tomato, 1));
        salad.addIngredient(ingredient(cheese, 1));
        dishRepository.save(salad);

        Dish risotto = dish("Chicken Risotto");
        risotto.addIngredient(ingredient(chicken, 2));
        risotto.addIngredient(ingredient(rice, 3));
        dishRepository.save(risotto);

        Dish soup = dish("Tomato Soup");
        soup.addIngredient(ingredient(tomato, 2));
        soup.addIngredient(ingredient(milk, 1));
        dishRepository.save(soup);

        Dish cheesePlate = dish("Cheese Plate");
        cheesePlate.addIngredient(ingredient(cheese, 2));
        dishRepository.save(cheesePlate);

        appCacheService.evictAll();
        stockEventService.publish("SEED");
        return new SeedResultDto(5, 3, 4, "demo data loaded");
    }

    private ProductInfo info(String title, String unit) {
        ProductInfo p = new ProductInfo();
        p.setTitle(title);
        p.setUnit(unit);
        p.setIngredients(new ArrayList<>());
        return p;
    }

    private Shipment shipment(String number, Instant date) {
        Shipment s = new Shipment();
        s.setShipmentNumber(number);
        s.setDateShipment(date);
        s.setProducts(new ArrayList<>());
        return s;
    }

    private Product batch(ProductInfo info, int qty, Instant exp) {
        Product p = new Product();
        p.setProductInfo(info);
        p.setQuantity(qty);
        p.setExpirationDate(exp);
        return p;
    }

    private Dish dish(String title) {
        Dish d = new Dish();
        d.setTitle(title);
        d.setDishIngredients(new ArrayList<>());
        d.setItems(new ArrayList<>());
        return d;
    }

    private DishIngredient ingredient(ProductInfo info, int qty) {
        DishIngredient i = new DishIngredient();
        i.setProductInfo(info);
        i.setQuantity(qty);
        return i;
    }
}
