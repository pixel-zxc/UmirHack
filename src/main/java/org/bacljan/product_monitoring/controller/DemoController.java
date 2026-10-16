package org.bacljan.product_monitoring.controller;

import lombok.AllArgsConstructor;
import org.bacljan.product_monitoring.DTO.SeedResultDto;
import org.bacljan.product_monitoring.service.DemoSeedService;
import org.bacljan.product_monitoring.service.StockEventService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DemoController {
    private final DemoSeedService demoSeedService;
    private final StockEventService stockEventService;

    @PostMapping("/demo/seed")
    public ResponseEntity<SeedResultDto> seed() {
        return ResponseEntity.ok(demoSeedService.seed());
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter events() {
        return stockEventService.subscribe();
    }
}
