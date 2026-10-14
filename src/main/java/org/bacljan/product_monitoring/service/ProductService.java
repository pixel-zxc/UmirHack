package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.AnalyticsDto;
import org.bacljan.product_monitoring.DTO.HotOfferDto;
import org.bacljan.product_monitoring.DTO.StockItemDto;
import org.bacljan.product_monitoring.model.*;
import org.bacljan.product_monitoring.repository.DishRepository;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.bacljan.product_monitoring.repository.WriteOffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final DishRepository dishRepository;
    private final WriteOffRepository writeOffRepository;

    public Map<Long, Integer> getStocks() {
        return repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0)
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getProductInfo().getId(),
                        Collectors.summingInt(Product::getQuantity)
                ));
    }

    public List<StockItemDto> getStockItems() {
        Map<Long, Integer> quantities = new HashMap<>();
        Map<Long, String> titles = new HashMap<>();
        for (Product p : repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0)) {
            Long id = p.getProductInfo().getId();
            quantities.merge(id, p.getQuantity(), Integer::sum);
            titles.putIfAbsent(id, p.getProductInfo().getTitle());
        }
        List<StockItemDto> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : quantities.entrySet()) {
            result.add(new StockItemDto(e.getKey(), titles.get(e.getKey()), e.getValue()));
        }
        return result;
    }

    public List<Product> getAllBatches() {
        return repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0);
    }

    public List<Product> getBatchesByProductId(Long productId) {
        return repository.findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(productId, 0);
    }

    public List<Dish> getStopList() {
        Map<Long, Integer> stocks = getStocks();
        List<Dish> stopList = new ArrayList<>();
        Instant now = Instant.now();

        for (Dish dish : dishRepository.findAll()) {
            boolean canCook = true;
            if (dish.getDishIngredients() == null || dish.getDishIngredients().isEmpty()) {
                canCook = false;
            } else {
                for (DishIngredient ingredient : dish.getDishIngredients()) {
                    Long productInfoId = ingredient.getProductInfo().getId();
                    int need = ingredient.getQuantity();
                    int available = stocks.getOrDefault(productInfoId, 0);
                    if (available < need) {
                        canCook = false;
                        break;
                    }
                    List<Product> batches = repository
                            .findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(productInfoId, 0);
                    boolean hasValid = batches.stream()
                            .anyMatch(b -> b.getExpirationDate().isAfter(now));
                    if (!hasValid) {
                        canCook = false;
                        break;
                    }
                }
            }
            if (!canCook) {
                stopList.add(dish);
            }
        }
        return stopList;
    }

    public List<Product> getExpiringBatches(int days) {
        Instant now = Instant.now();
        Instant threshold = now.plus(days, ChronoUnit.DAYS);
        return repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0)
                .stream()
                .filter(p -> !p.getExpirationDate().isAfter(threshold) && p.getExpirationDate().isAfter(now))
                .toList();
    }

    @Transactional
    public int writeOffExpired() {
        Instant now = Instant.now();
        List<Product> all = repository.findByQuantityGreaterThanOrderByExpirationDateAsc(0);
        int total = 0;
        for (Product batch : all) {
            if (!batch.getExpirationDate().isAfter(now)) {
                int qty = batch.getQuantity();
                if (qty > 0) {
                    WriteOff writeOff = new WriteOff();
                    writeOff.setCreatedAt(now);
                    writeOff.setQuantity(qty);
                    writeOff.setProduct(batch);
                    writeOff.setProductInfo(batch.getProductInfo());
                    writeOffRepository.save(writeOff);

                    batch.setQuantity(0);
                    total += qty;
                }
            }
        }
        return total;
    }

    public List<HotOfferDto> getHotOffers() {
        Map<Long, Integer> stocks = getStocks();
        Instant now = Instant.now();
        List<Dish> stopList = getStopList();
        Set<Long> stopIds = stopList.stream().map(Dish::getId).collect(Collectors.toSet());

        List<HotOfferDto> offers = new ArrayList<>();

        for (Dish dish : dishRepository.findAll()) {
            if (stopIds.contains(dish.getId())) continue;
            if (dish.getDishIngredients() == null || dish.getDishIngredients().isEmpty()) continue;

            double score = 0;
            String bestReason = null;
            double bestUrgency = -1;
            int totalRecipeQty = dish.getDishIngredients().stream()
                    .mapToInt(DishIngredient::getQuantity)
                    .sum();

            for (DishIngredient ingredient : dish.getDishIngredients()) {
                Long productInfoId = ingredient.getProductInfo().getId();
                int stock = stocks.getOrDefault(productInfoId, 0);
                if (stock <= 0) continue;

                List<Product> batches = repository
                        .findByProductInfoIdAndQuantityGreaterThanOrderByExpirationDateAsc(productInfoId, 0);
                if (batches.isEmpty()) continue;

                Product earliest = batches.get(0);
                long daysLeft = ChronoUnit.DAYS.between(now, earliest.getExpirationDate());
                if (daysLeft < 0) continue;

                double urgency = 1.0 / (daysLeft + 1);
                double residual = Math.min(1.0, (double) stock / (ingredient.getQuantity() * 10));
                double share = totalRecipeQty == 0 ? 0 : (double) ingredient.getQuantity() / totalRecipeQty;

                double part = urgency * residual * (0.5 + share);
                score += part;

                if (urgency > bestUrgency) {
                    bestUrgency = urgency;
                    bestReason = ingredient.getProductInfo().getTitle()
                            + " expires in " + daysLeft + " day(s), stock=" + stock;
                }
            }

            if (score > 0 && bestReason != null) {
                offers.add(new HotOfferDto(dish.getId(), dish.getTitle(), score, bestReason));
            }
        }

        offers.sort((a, b) -> Double.compare(b.getPriority(), a.getPriority()));
        return offers;
    }

    public AnalyticsDto getAnalytics() {
        Long total = writeOffRepository.sumAllQuantity();
        if (total == null) total = 0L;

        Map<Long, Integer> stocks = getStocks();
        Map<Long, Integer> usage = new HashMap<>();
        for (Dish dish : dishRepository.findAll()) {
            if (dish.getDishIngredients() == null) continue;
            for (DishIngredient ing : dish.getDishIngredients()) {
                usage.merge(ing.getProductInfo().getId(), 1, Integer::sum);
            }
        }

        List<Map<String, Object>> scarce = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : stocks.entrySet()) {
            Long id = e.getKey();
            int stock = e.getValue();
            int dishes = usage.getOrDefault(id, 0);
            if (stock < 20 || (dishes > 0 && stock < dishes * 5)) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("productInfoId", id);
                row.put("stock", stock);
                row.put("usedInDishes", dishes);
                scarce.add(row);
            }
        }
        scarce.sort((a, b) -> Integer.compare((int) b.get("usedInDishes"), (int) a.get("usedInDishes")));

        return new AnalyticsDto(total, scarce);
    }
}
