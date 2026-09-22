package com.rentflow.booking.mapper;

import com.rentflow.booking.domain.Booking;
import com.rentflow.booking.dto.BookingResponse;
import com.rentflow.booking.dto.CreateBookingRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(CreateBookingRequest request) {
        return new Booking(
                request.propertyId(),
                request.guestId(),
                request.checkIn(),
                request.checkOut(),
                request.totalPrice(),
                request.source()
        );
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getPropertyId(),
                booking.getGuestId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getSource(),
                booking.getCreatedAt()
        );
    }
}