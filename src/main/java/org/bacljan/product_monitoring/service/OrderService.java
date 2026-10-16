package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.OrderDto;
import org.bacljan.product_monitoring.DTO.OrderItemDto;
import org.bacljan.product_monitoring.exception.IllegalProductQuantityException;
import org.bacljan.product_monitoring.exception.OrderNotFoundException;
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
    private final ProductInfoService productInfoService;
    private final StockEventService stockEventService;
    private final AppCacheService appCacheService;

    @Transactional
    public Order createOrder(OrderDto dto) {
        if (dto == null) throw new NullPointerException("Order DTO cannot be a null");
        if (dto.getDtos() == null || dto.getDtos().isEmpty()) {
            throw new IllegalProductQuantityException("order items cannot be empty");
        }

        Map<Long, Integer> required = new HashMap<>();
        for (OrderItemDto itemDto : dto.getDtos()) {
            if (itemDto.getQuantity() < 1) {
                throw new IllegalProductQuantityException("quantity must be at least 1");
            }
            Dish dish = dishService.findById(itemDto.getDishId());
            if (dish.getDishIngredients() == null || dish.getDishIngredients().isEmpty()) {
                throw new IllegalProductQuantityException("dish has no ingredients");
            }
            for (DishIngredient ingredient : dish.getDishIngredients()) {
                long productInfoId = ingredient.getProductInfo().getId();
                int need = ingredient.getQuantity() * itemDto.getQuantity();
                required.merge(productInfoId, need, Integer::sum);
            }
        }

        Instant now = Instant.now();
        Map<Long, List<Product>> batchesByProduct = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : required.entrySet()) {
            Long productInfoId = entry.getKey();
            int need = entry.getValue();
            List<Product> batches = productRepository.findActiveBatchesByProductInfoId(productInfoId);

            List<Product> validBatches = new ArrayList<>();
            int available = 0;
            for (Product batch : batches) {
                if (batch.getExpirationDate().isAfter(now)) {
                    validBatches.add(batch);
                    available += batch.getQuantity();
                }
            }

            if (available < need) {
                throw new IllegalProductQuantityException(
                        "Not enough " + titleOf(productInfoId, batches) + ": need=" + need + ", available=" + available);
            }
            batchesByProduct.put(productInfoId, validBatches);
        }

        for (Map.Entry<Long, Integer> entry : required.entrySet()) {
            int need = entry.getValue();
            List<Product> batches = batchesByProduct.get(entry.getKey());
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

        Order saved = repository.save(order);
        touch(saved);
        appCacheService.evictStockViews();
        stockEventService.publish("ORDER_CREATED");
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> list(String status) {
        OrderStatus parsed = null;
        if (status != null && !status.isBlank()) {
            try {
                parsed = OrderStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalProductQuantityException("unknown status: " + status);
            }
        }
        List<Order> orders = parsed == null
                ? repository.findAllByOrderByCreatedAtDesc()
                : repository.findByStatusOrderByCreatedAtDesc(parsed);
        orders.forEach(this::touch);
        return orders;
    }

    @Transactional
    public Order complete(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("order not found"));
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalProductQuantityException(
                    "order status is " + order.getStatus() + ", expected CREATED");
        }
        order.setStatus(OrderStatus.COMPLETED);
        touch(order);
        stockEventService.publish("ORDER_COMPLETED");
        return order;
    }

    private String titleOf(Long productInfoId, List<Product> batches) {
        if (batches != null) {
            for (Product batch : batches) {
                if (batch.getProductInfo() != null
                        && batch.getProductInfo().getTitle() != null
                        && !batch.getProductInfo().getTitle().isBlank()) {
                    return batch.getProductInfo().getTitle();
                }
            }
        }
        ProductInfo info = productInfoService.getById(productInfoId);
        if (info.getTitle() != null && !info.getTitle().isBlank()) {
            return info.getTitle();
        }
        return "product " + productInfoId;
    }

    private void touch(Order order) {
        if (order.getItems() == null) return;
        order.getItems().forEach(item -> {
            if (item.getDish() != null) {
                item.getDish().getTitle();
            }
        });
    }
}
