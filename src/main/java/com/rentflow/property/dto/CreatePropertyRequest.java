package com.rentflow.property.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePropertyRequest(
        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        @Size(max = 500)
        String address,

        @NotNull
        @Min(1)
        Integer maxGuests,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal defaultPrice
) {
}