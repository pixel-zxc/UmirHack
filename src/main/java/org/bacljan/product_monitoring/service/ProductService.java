package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.AnalyticsDto;
import org.bacljan.product_monitoring.DTO.HotOfferDto;
import org.bacljan.product_monitoring.DTO.StockItemDto;
import org.bacljan.product_monitoring.DTO.StopListForecastDto;
import org.bacljan.product_monitoring.model.*;
import org.bacljan.product_monitoring.repository.DishRepository;
import org.bacljan.product_monitoring.repository.ProductRepository;
import org.bacljan.product_monitoring.repository.WriteOffRepository;
import org.springframework.cache.annotation.Cacheable;
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
    private final StockEventService stockEventService;
    private final AppCacheService appCacheService;

    @Cacheable("stocks")
    public Map<Long, Integer> getStocks() {
        Instant now = Instant.now();
        return repository.findActiveBatches()
                .stream()
                .filter(p -> p.getExpirationDate().isAfter(now))
                .collect(Collectors.groupingBy(
                        p -> p.getProductInfo().getId(),
                        Collectors.summingInt(Product::getQuantity)
                ));
    }

    @Cacheable(value = "stocks", key = "'items'")
    public List<StockItemDto> getStockItems() {
        Instant now = Instant.now();
        Map<Long, Integer> quantities = new HashMap<>();
        Map<Long, String> titles = new HashMap<>();
        for (Product p : repository.findActiveBatches()) {
            if (!p.getExpirationDate().isAfter(now)) continue;
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

    @Cacheable("batches")
    public List<Product> getAllBatches() {
        return repository.findActiveBatches();
    }

    @Cacheable(value = "batchesByProduct", key = "#productId")
    public List<Product> getBatchesByProductId(Long productId) {
        return repository.findActiveBatchesByProductInfoId(productId);
    }

    @Transactional(readOnly = true)
    @Cacheable("stoplist")
    public List<Dish> getStopList() {
        Map<Long, Integer> stocks = getStocks();
        List<Dish> stopList = new ArrayList<>();

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
                }
            }
            if (!canCook) {
                stopList.add(dish);
            }
        }
        return stopList;
    }

    @Cacheable(value = "expiring", key = "#days")
    public List<Product> getExpiringBatches(int days) {
        Instant now = Instant.now();
        Instant threshold = now.plus(days, ChronoUnit.DAYS);
        return repository.findActiveBatches()
                .stream()
                .filter(p -> !p.getExpirationDate().isAfter(threshold) && p.getExpirationDate().isAfter(now))
                .toList();
    }

    @Transactional
    public int writeOffExpired() {
        Instant now = Instant.now();
        List<Product> all = repository.findActiveBatches();
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
        if (total > 0) {
            stockEventService.publish("WRITE_OFF");
        }
        appCacheService.evictStockViews();
        return total;
    }

    @Transactional(readOnly = true)
    @Cacheable("hotOffers")
    public List<HotOfferDto> getHotOffers() {
        Map<Long, Integer> stocks = getStocks();
        Instant now = Instant.now();
        Set<Long> stopIds = getStopList().stream().map(Dish::getId).collect(Collectors.toSet());

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
                        .findActiveBatchesByProductInfoId(productInfoId)
                        .stream()
                        .filter(b -> b.getExpirationDate().isAfter(now))
                        .toList();
                if (batches.isEmpty()) continue;

                Product earliest = batches.get(0);
                long daysLeft = ChronoUnit.DAYS.between(now, earliest.getExpirationDate());
                if (daysLeft < 0) continue;

                double urgency = 1.0 / (daysLeft + 1);
                double residual = Math.min(1.0, (double) stock / (ingredient.getQuantity() * 10.0));
                double share = totalRecipeQty == 0 ? 0 : (double) ingredient.getQuantity() / totalRecipeQty;
                score += urgency * residual * (0.5 + share);

                if (urgency > bestUrgency) {
                    bestUrgency = urgency;
                    String unit = ingredient.getProductInfo().getUnit() != null
                            ? ingredient.getProductInfo().getUnit()
                            : "pcs";
                    bestReason = ingredient.getProductInfo().getTitle()
                            + " expires in " + daysLeft + " day(s), stock=" + stock + " " + unit;
                }
            }

            if (score > 0 && bestReason != null) {
                offers.add(new HotOfferDto(dish.getId(), dish.getTitle(),
                        Math.round(score * 1000.0) / 1000.0, bestReason));
            }
        }

        offers.sort((a, b) -> Double.compare(b.getPriority(), a.getPriority()));
        return offers;
    }

    @Transactional(readOnly = true)
    @Cacheable("forecast")
    public List<StopListForecastDto> getStopListForecast() {
        Map<Long, Integer> stocks = getStocks();
        Set<Long> stopIds = getStopList().stream().map(Dish::getId).collect(Collectors.toSet());
        List<StopListForecastDto> result = new ArrayList<>();

        for (Dish dish : dishRepository.findAll()) {
            if (stopIds.contains(dish.getId())) continue;
            if (dish.getDishIngredients() == null || dish.getDishIngredients().isEmpty()) continue;

            int minOrders = Integer.MAX_VALUE;
            String limitingTitle = null;
            int limitingStock = 0;
            int limitingNeed = 0;

            for (DishIngredient ingredient : dish.getDishIngredients()) {
                Long productInfoId = ingredient.getProductInfo().getId();
                int stock = stocks.getOrDefault(productInfoId, 0);
                int need = ingredient.getQuantity();
                if (need <= 0) continue;
                int canMake = stock / need;
                if (canMake < minOrders) {
                    minOrders = canMake;
                    limitingTitle = ingredient.getProductInfo().getTitle();
                    limitingStock = stock;
                    limitingNeed = need;
                }
            }

            if (minOrders == Integer.MAX_VALUE) continue;
            result.add(new StopListForecastDto(
                    dish.getId(),
                    dish.getTitle(),
                    minOrders,
                    limitingTitle,
                    limitingStock,
                    limitingNeed
            ));
        }

        result.sort(Comparator.comparing(StopListForecastDto::getOrdersLeft));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable("analytics")
    public AnalyticsDto getAnalytics() {
        Long total = writeOffRepository.sumAllQuantity();
        if (total == null) total = 0L;

        Map<Long, Integer> stocks = getStocks();
        Map<Long, Integer> usage = new HashMap<>();
        Map<Long, String> titles = new HashMap<>();
        for (Dish dish : dishRepository.findAll()) {
            if (dish.getDishIngredients() == null) continue;
            for (DishIngredient ing : dish.getDishIngredients()) {
                Long id = ing.getProductInfo().getId();
                usage.merge(id, 1, Integer::sum);
                titles.putIfAbsent(id, ing.getProductInfo().getTitle());
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
                row.put("title", titles.get(id));
                row.put("stock", stock);
                row.put("usedInDishes", dishes);
                scarce.add(row);
            }
        }
        scarce.sort((a, b) -> Integer.compare((int) b.get("usedInDishes"), (int) a.get("usedInDishes")));
        return new AnalyticsDto(total, scarce);
    }
}
