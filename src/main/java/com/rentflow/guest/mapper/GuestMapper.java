package com.rentflow.guest.mapper;

import com.rentflow.guest.domain.Guest;
import com.rentflow.guest.dto.CreateGuestRequest;
import com.rentflow.guest.dto.GuestResponse;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public Guest toEntity(CreateGuestRequest request) {
        return new Guest(
                request.firstName(),
                request.lastName(),
                request.phone()
        );
    }

    public GuestResponse toResponse(Guest guest) {
        return new GuestResponse(
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getPhone(),
                guest.getCreatedAt()
        );
    }
}