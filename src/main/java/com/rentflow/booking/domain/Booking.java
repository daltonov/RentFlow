package com.rentflow.booking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "property_id", nullable = false)
    private UUID propertyId;

    @Column(name = "guest_id", nullable = false)
    private UUID guestId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BookingSource source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Booking() {
    }

    public Booking(UUID propertyId, UUID guestId,
                   LocalDate checkIn, LocalDate checkOut,
                   BigDecimal totalPrice, BookingSource source) {
        this.propertyId = propertyId;
        this.guestId = guestId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.source = source;
        this.status = BookingStatus.NEW;
    }

    @PrePersist
    private void beforeInsert() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public BookingSource getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}