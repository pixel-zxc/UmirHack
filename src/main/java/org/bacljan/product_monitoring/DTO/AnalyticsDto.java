package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@AllArgsConstructor
public class AnalyticsDto {
    long totalWrittenOff;
    List<Map<String, Object>> scarceProducts;
}
