package com.rentflow.booking.dto;

import com.rentflow.booking.domain.BookingSource;
import com.rentflow.booking.domain.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID propertyId,
        UUID guestId,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal totalPrice,
        BookingStatus status,
        BookingSource source,
        Instant createdAt
) {
}