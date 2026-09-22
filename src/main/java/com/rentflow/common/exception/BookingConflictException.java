package com.rentflow.common.exception;

import java.util.UUID;

public class BookingConflictException extends RuntimeException {

    public BookingConflictException(UUID propertyId) {
        super("Property " + propertyId
                + " is already booked for the requested dates");
    }
}