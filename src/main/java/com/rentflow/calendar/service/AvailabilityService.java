package com.rentflow.calendar.service;

import com.rentflow.calendar.domain.StayPeriod;
import com.rentflow.calendar.spi.OccupancyQuery;
import com.rentflow.property.service.PropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AvailabilityService {

    private final PropertyService propertyService;
    private final OccupancyQuery occupancyQuery;

    public AvailabilityService(PropertyService propertyService,
                               OccupancyQuery occupancyQuery) {
        this.propertyService = propertyService;
        this.occupancyQuery = occupancyQuery;
    }

    public boolean isAvailable(UUID propertyId,
                               LocalDate checkIn,
                               LocalDate checkOut) {
        StayPeriod period = new StayPeriod(checkIn, checkOut);

        propertyService.getById(propertyId);

        return !occupancyQuery.hasOverlap(propertyId, period);
    }
}