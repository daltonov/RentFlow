package com.rentflow.calendar.controller;

import com.rentflow.calendar.dto.AvailabilityResponse;
import com.rentflow.calendar.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties/{id}/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public AvailabilityResponse getAvailability(
            @PathVariable UUID id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut) {

        boolean available = availabilityService.isAvailable(
                id,
                checkIn,
                checkOut
        );

        return new AvailabilityResponse(
                id,
                checkIn,
                checkOut,
                available
        );
    }
}