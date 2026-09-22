package com.rentflow.calendar.domain;

import com.rentflow.common.exception.InvalidDateRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StayPeriodTest {

    private final LocalDate checkIn = LocalDate.of(2026, 10, 1);

    @Test
    void acceptsOneNightStay() {
        assertDoesNotThrow(() ->
                new StayPeriod(checkIn, checkIn.plusDays(1))
        );
    }

    @Test
    void rejectsSameDayCheckout() {
        assertThrows(InvalidDateRangeException.class, () ->
                new StayPeriod(checkIn, checkIn)
        );
    }

    @Test
    void rejectsCheckoutBeforeCheckIn() {
        assertThrows(InvalidDateRangeException.class, () ->
                new StayPeriod(checkIn, checkIn.minusDays(1))
        );
    }

    @Test
    void rejectsMissingCheckIn() {
        assertThrows(InvalidDateRangeException.class, () ->
                new StayPeriod(null, checkIn)
        );
    }

    @Test
    void rejectsMissingCheckOut() {
        assertThrows(InvalidDateRangeException.class, () ->
                new StayPeriod(checkIn, null)
        );
    }
}