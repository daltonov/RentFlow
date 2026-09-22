package com.rentflow.calendar.domain;

import com.rentflow.common.exception.InvalidDateRangeException;

import java.time.LocalDate;

public record StayPeriod(LocalDate checkIn, LocalDate checkOut) {

    public StayPeriod {
        if (checkIn == null
                || checkOut == null
                || !checkOut.isAfter(checkIn)) {
            throw new InvalidDateRangeException();
        }
    }
}