package com.rentflow.booking.domain;

public enum BookingStatus {
    NEW,
    CONTACTED,
    WAITING_FOR_PAYMENT,
    CONFIRMED,
    CHECK_IN_READY,
    CHECKED_IN,
    CHECKED_OUT,
    COMPLETED,
    CANCELLED,
    EXPIRED
}