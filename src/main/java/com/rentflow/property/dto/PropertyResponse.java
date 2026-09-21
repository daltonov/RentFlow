package com.rentflow.property.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PropertyResponse(
        UUID id,
        String name,
        String address,
        Integer maxGuests,
        BigDecimal defaultPrice,
        Instant createdAt
) {
}