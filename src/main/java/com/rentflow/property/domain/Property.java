package com.rentflow.property.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    @Column(name = "default_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal defaultPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Property() {
    }

    public Property(String name, String address,
                    Integer maxGuests, BigDecimal defaultPrice) {
        this.name = name;
        this.address = address;
        this.maxGuests = maxGuests;
        this.defaultPrice = defaultPrice;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    private void beforeInsert() {
        createdAt = Instant.now();
    }
}