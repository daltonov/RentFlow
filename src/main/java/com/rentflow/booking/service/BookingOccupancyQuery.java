package com.rentflow.booking.service;

import com.rentflow.booking.domain.BookingStatus;
import com.rentflow.booking.repository.BookingRepository;
import com.rentflow.calendar.domain.StayPeriod;
import com.rentflow.calendar.spi.OccupancyQuery;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class BookingOccupancyQuery implements OccupancyQuery {

    private static final Set<BookingStatus> NON_BLOCKING_STATUSES =
            Set.of(BookingStatus.CANCELLED, BookingStatus.EXPIRED);

    private final BookingRepository bookingRepository;

    public BookingOccupancyQuery(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean hasOverlap(UUID propertyId, StayPeriod period) {
        return bookingRepository.existsOverlap(
                propertyId,
                period.checkIn(),
                period.checkOut(),
                NON_BLOCKING_STATUSES
        );
    }
}