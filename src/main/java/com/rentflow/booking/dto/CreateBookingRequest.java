package com.rentflow.booking.dto;

import com.rentflow.booking.domain.BookingSource;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateBookingRequest(
        @NotNull
        UUID propertyId,

        @NotNull
        UUID guestId,

        @NotNull
        LocalDate checkIn,

        @NotNull
        LocalDate checkOut,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal totalPrice,

        @NotNull
        BookingSource source
) {
}