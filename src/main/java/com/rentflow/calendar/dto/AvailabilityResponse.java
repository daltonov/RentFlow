package com.rentflow.calendar.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AvailabilityResponse(
        UUID propertyId,
        LocalDate checkIn,
        LocalDate checkOut,
        boolean available
) {
}