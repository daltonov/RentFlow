package com.rentflow.property.service;

import com.rentflow.property.dto.PropertyResponse;
import com.rentflow.property.mapper.PropertyMapper;
import com.rentflow.property.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rentflow.property.domain.Property;
import com.rentflow.property.dto.CreatePropertyRequest;
import com.rentflow.common.exception.NotFoundException;
import org.springframework.transaction.annotation.Propagation;
import java.util.UUID;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    @Transactional(propagation = Propagation.MANDATORY)
    public void lockForBooking(UUID id) {
        propertyRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Property", id));
    }

    public PropertyService(PropertyRepository propertyRepository,
                           PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public List<PropertyResponse> getAll() {
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    @Transactional
    public PropertyResponse create(CreatePropertyRequest request) {
        Property property = propertyMapper.toEntity(request);
        Property savedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(savedProperty);
    }

    public PropertyResponse getById(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Property", id));

        return propertyMapper.toResponse(property);
    }
}