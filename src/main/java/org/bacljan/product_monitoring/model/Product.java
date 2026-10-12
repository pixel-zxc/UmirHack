package org.bacljan.product_monitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Table(name="products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;
    @Column(name = "units")
    private String unit;
    @ManyToOne
    @JoinColumn(name="shipment_id")
    private Shipment shipment;
}
