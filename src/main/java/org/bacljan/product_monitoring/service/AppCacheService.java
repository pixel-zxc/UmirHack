package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.config.CacheConfig;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сброс кэшей после изменений склада / меню.
 */
@Service
@AllArgsConstructor
public class AppCacheService {
    private final CacheManager cacheManager;

    private static final List<String> STOCK_VIEWS = List.of(
            CacheConfig.STOCKS,
            CacheConfig.BATCHES,
            CacheConfig.BATCHES_BY_PRODUCT,
            CacheConfig.EXPIRING,
            CacheConfig.STOPLIST,
            CacheConfig.FORECAST,
            CacheConfig.HOT_OFFERS,
            CacheConfig.ANALYTICS
    );

    public void evictStockViews() {
        clear(STOCK_VIEWS);
    }

    public void evictDishes() {
        clear(List.of(CacheConfig.DISHES, CacheConfig.STOPLIST, CacheConfig.FORECAST, CacheConfig.HOT_OFFERS, CacheConfig.ANALYTICS));
    }

    public void evictProducts() {
        clear(List.of(CacheConfig.PRODUCTS, CacheConfig.STOCKS, CacheConfig.ANALYTICS));
    }

    public void evictAll() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache c = cacheManager.getCache(name);
            if (c != null) c.clear();
        });
    }

    private void clear(List<String> names) {
        for (String name : names) {
            Cache c = cacheManager.getCache(name);
            if (c != null) c.clear();
        }
    }
}
