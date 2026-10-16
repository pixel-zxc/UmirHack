package org.bacljan.product_monitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StopListForecastDto {
    long dishId;
    String dishTitle;
    Integer ordersLeft;
    String limitingProduct;
    int limitingStock;
    int qtyPerOrder;
}
