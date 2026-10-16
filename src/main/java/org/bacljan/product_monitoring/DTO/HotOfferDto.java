package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class HotOfferDto {
    /** null для автоматически рассчитанных предложений */
    Long id;
    long dishId;
    String dishTitle;
    double priority;
    String reason;
    /** MANUAL — создано админом, AUTO — рассчитано по срокам */
    String source;
}
