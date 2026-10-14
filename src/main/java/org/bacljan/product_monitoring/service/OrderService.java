package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.OrderDto;
import org.bacljan.product_monitoring.DTO.OrderItemDto;
import org.bacljan.product_monitoring.exception.IllegalProductQuantityException;
import org.bacljan.product_monitoring.exception.ProductIsExirationException;
import org.bacljan.product_monitoring.model.*;
import org.bacljan.product_monitoring.repository.OrderRepository;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final DishService dishService;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderDto dto) {
        if (dto == null) throw new NullPointerException("Order DTO cannot be a null");

        Map<Long, Integer> required = new HashMap<>();
        for (OrderItemDto itemDto : dto.getDtos()) {
            Dish dish = dishService.findById(itemDto.getDishId());
            for (DishIngredient ingredient : dish.getDishIngredients()) {
                long productInfoId = ingredient.getProductInfo().getId();
                int need = ingredient.getQuantity() * itemDto.getQuantity();
                required.merge(productInfoId, need, Integer::sum);
            }
        }

        Map<Long, List<Product>> batchesByProduct = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : required.entrySet()) {
            Long productInfoId = entry.getKey();
            int need = entry.getValue();
            List<Product> batches = productRepository
                    .findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(productInfoId, 0);

            Instant now = Instant.now();
            List<Product> validBatches = new ArrayList<>();
            int available = 0;
            for (Product batch : batches) {
                if (batch.getExpirationDate().isBefore(now) || batch.getExpirationDate().equals(now)) {
                    throw new ProductIsExirationException("product is expired");
                }
                validBatches.add(batch);
                available += batch.getQuantity();
            }

            if (available < need) {
                throw new IllegalProductQuantityException("not enough products");
            }
            batchesByProduct.put(productInfoId, validBatches);
        }

        for (Map.Entry<Long, Integer> entry : required.entrySet()) {
            Long productInfoId = entry.getKey();
            int need = entry.getValue();
            List<Product> batches = batchesByProduct.get(productInfoId);
            for (Product batch : batches) {
                if (need <= 0) break;
                int take = Math.min(batch.getQuantity(), need);
                batch.setQuantity(batch.getQuantity() - take);
                need -= take;
            }
        }

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());

        for (OrderItemDto itemDto : dto.getDtos()) {
            Dish dish = dishService.findById(itemDto.getDishId());
            OrderItem item = new OrderItem();
            item.setQuantity(itemDto.getQuantity());
            item.setDish(dish);
            order.addItem(item);
        }

        return repository.save(order);
    }
}
