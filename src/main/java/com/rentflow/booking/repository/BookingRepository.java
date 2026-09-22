package com.rentflow.booking.repository;

import com.rentflow.booking.domain.Booking;
import com.rentflow.booking.domain.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
            select (count(b) > 0)
            from Booking b
            where b.propertyId = :propertyId
              and b.status not in :excludedStatuses
              and b.checkIn < :checkOut
              and b.checkOut > :checkIn
            """)
    boolean existsOverlap(
            @Param("propertyId") UUID propertyId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("excludedStatuses") Collection<BookingStatus> excludedStatuses
    );
}
