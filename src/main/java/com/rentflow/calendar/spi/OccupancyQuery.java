package com.rentflow.calendar.spi;

import com.rentflow.calendar.domain.StayPeriod;

import java.util.UUID;

public interface OccupancyQuery {

    boolean hasOverlap(UUID propertyId, StayPeriod period);
}