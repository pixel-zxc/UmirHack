package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExpiredWriteOffJob {
    private final ProductService productService;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        productService.writeOffExpired();
    }
}
