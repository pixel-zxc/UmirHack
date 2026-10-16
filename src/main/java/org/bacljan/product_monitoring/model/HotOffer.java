package org.bacljan.product_monitoring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "hot_offers")
@Getter
@Setter
public class HotOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    /** Приоритет (чем выше — тем заметнее в списке) */
    private double priority;

    @Column(length = 500)
    private String reason;

    private Instant createdAt;

    /** true = показывается в списке горячих */
    private boolean active = true;
}
