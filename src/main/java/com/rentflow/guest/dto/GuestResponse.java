package com.rentflow.guest.dto;

import java.time.Instant;
import java.util.UUID;

public record GuestResponse(
        UUID id,
        String firstName,
        String lastName,
        String phone,
        Instant createdAt
) {
}