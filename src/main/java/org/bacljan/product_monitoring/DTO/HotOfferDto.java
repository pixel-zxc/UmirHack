package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class HotOfferDto {
    long dishId;
    String dishTitle;
    double priority;
    String reason;
}
