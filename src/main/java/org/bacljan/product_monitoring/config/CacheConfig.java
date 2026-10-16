package org.bacljan.product_monitoring.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String STOCKS = "stocks";
    public static final String BATCHES = "batches";
    public static final String BATCHES_BY_PRODUCT = "batchesByProduct";
    public static final String EXPIRING = "expiring";
    public static final String STOPLIST = "stoplist";
    public static final String FORECAST = "forecast";
    public static final String HOT_OFFERS = "hotOffers";
    public static final String ANALYTICS = "analytics";
    public static final String DISHES = "dishes";
    public static final String PRODUCTS = "products";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                STOCKS,
                BATCHES,
                BATCHES_BY_PRODUCT,
                EXPIRING,
                STOPLIST,
                FORECAST,
                HOT_OFFERS,
                ANALYTICS,
                DISHES,
                PRODUCTS
        );
    }
}
