package org.bacljan.product_monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductMonitoringApplication.class, args);
    }

}
