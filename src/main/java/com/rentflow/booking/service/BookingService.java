package com.rentflow.booking.service;

import com.rentflow.booking.domain.Booking;
import com.rentflow.booking.dto.BookingResponse;
import com.rentflow.booking.dto.CreateBookingRequest;
import com.rentflow.booking.mapper.BookingMapper;
import com.rentflow.booking.repository.BookingRepository;
import com.rentflow.calendar.domain.StayPeriod;
import com.rentflow.calendar.service.AvailabilityService;
import com.rentflow.common.exception.BookingConflictException;
import com.rentflow.common.exception.NotFoundException;
import com.rentflow.guest.service.GuestService;
import com.rentflow.property.service.PropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PropertyService propertyService;
    private final GuestService guestService;
    private final AvailabilityService availabilityService;

    public BookingService(BookingRepository bookingRepository,
                          BookingMapper bookingMapper,
                          PropertyService propertyService,
                          GuestService guestService,
                          AvailabilityService availabilityService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.propertyService = propertyService;
        this.guestService = guestService;
        this.availabilityService = availabilityService;
    }

    public List<BookingResponse> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    public BookingResponse getById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking", id));

        return bookingMapper.toResponse(booking);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponse create(CreateBookingRequest request) {
        StayPeriod period = new StayPeriod(
                request.checkIn(),
                request.checkOut()
        );

        propertyService.lockForBooking(request.propertyId());
        guestService.getById(request.guestId());

        boolean available = availabilityService.isAvailable(
                request.propertyId(),
                period.checkIn(),
                period.checkOut()
        );

        if (!available) {
            throw new BookingConflictException(request.propertyId());
        }

        Booking booking = bookingMapper.toEntity(request);
        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }
}