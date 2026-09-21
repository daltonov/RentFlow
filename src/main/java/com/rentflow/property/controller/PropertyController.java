package com.rentflow.property.controller;

import com.rentflow.property.dto.PropertyResponse;
import com.rentflow.property.service.PropertyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rentflow.property.dto.CreatePropertyRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @GetMapping("/{id}")
    public PropertyResponse getById(@PathVariable UUID id) {
        return propertyService.getById(id);
    }

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<PropertyResponse> getAll() {
        return propertyService.getAll();
    }

    @PostMapping
    public ResponseEntity<PropertyResponse> create(
            @Valid @RequestBody CreatePropertyRequest request) {

        PropertyResponse response = propertyService.create(request);

        return ResponseEntity
                .created(URI.create("/api/properties/" + response.id()))
                .body(response);
    }
}