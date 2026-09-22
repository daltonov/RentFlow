package com.rentflow.guest.controller;

import com.rentflow.guest.dto.CreateGuestRequest;
import com.rentflow.guest.dto.GuestResponse;
import com.rentflow.guest.service.GuestService;
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
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public List<GuestResponse> getAll() {
        return guestService.getAll();
    }

    @GetMapping("/{id}")
    public GuestResponse getById(@PathVariable UUID id) {
        return guestService.getById(id);
    }

    @PostMapping
    public ResponseEntity<GuestResponse> create(
            @Valid @RequestBody CreateGuestRequest request) {

        GuestResponse response = guestService.create(request);

        return ResponseEntity
                .created(URI.create("/api/guests/" + response.id()))
                .body(response);
    }
}