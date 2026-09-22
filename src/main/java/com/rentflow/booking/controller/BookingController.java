package com.rentflow.booking.controller;

import com.rentflow.booking.dto.BookingResponse;
import com.rentflow.booking.dto.CreateBookingRequest;
import com.rentflow.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable UUID id) {
        return bookingService.getById(id);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody CreateBookingRequest request) {

        BookingResponse response = bookingService.create(request);

        return ResponseEntity
                .created(URI.create("/api/bookings/" + response.id()))
                .body(response);
    }
}