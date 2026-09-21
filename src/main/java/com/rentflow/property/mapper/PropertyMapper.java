package com.rentflow.property.mapper;

import com.rentflow.property.domain.Property;
import com.rentflow.property.dto.PropertyResponse;
import org.springframework.stereotype.Component;
import com.rentflow.property.dto.CreatePropertyRequest;

@Component
public class PropertyMapper {

    public PropertyResponse toResponse(Property property) {
        return new PropertyResponse(
                property.getId(),
                property.getName(),
                property.getAddress(),
                property.getMaxGuests(),
                property.getDefaultPrice(),
                property.getCreatedAt()
        );
    }

    public Property toEntity(CreatePropertyRequest request) {
        return new Property(
                request.name(),
                request.address(),
                request.maxGuests(),
                request.defaultPrice()
        );
    }
}