package org.bacljan.product_monitoring.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "shipment_number", nullable = false)
    private String shipmentNumber;
    @Column(name = "date_shipment", nullable = false)
    private Instant dateShipment;
    @OneToMany(mappedBy = "shipment")
    private List<Product> products;
}
