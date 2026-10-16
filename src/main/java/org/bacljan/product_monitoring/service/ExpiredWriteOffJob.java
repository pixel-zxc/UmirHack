package org.bacljan.product_monitoring.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Автоматическое списание просроченных партий.
 * Запускается при старте приложения и далее каждый час.
 */
@Component
@AllArgsConstructor
@Slf4j
public class ExpiredWriteOffJob {
    private final ProductService productService;

    /** Каждый час в :00 */
    @Scheduled(cron = "0 0 * * * *")
    public void runScheduled() {
        doWriteOff("scheduled");
    }

    /** Один раз после полного старта приложения */
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        doWriteOff("startup");
    }

    private void doWriteOff(String trigger) {
        try {
            int written = productService.writeOffExpired();
            if (written > 0) {
                log.info("Автосписание просрочки ({}): списано {} ед.", trigger, written);
            } else {
                log.debug("Автосписание просрочки ({}): нечего списывать", trigger);
            }
        } catch (Exception e) {
            log.error("Ошибка автосписания просрочки ({})", trigger, e);
        }
    }
}
