package org.bacljan.product_monitoring.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.List;


@Value

public class ShipmentDto {
    @NotBlank
    String shipmentNumber;
    @NotNull
    @PastOrPresent
    Instant dateShipment;
    @NotEmpty
    List<@Valid ProductDto> products;
}