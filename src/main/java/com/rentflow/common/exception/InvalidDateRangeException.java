package com.rentflow.common.exception;

public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException() {
        super("checkIn and checkOut are required; checkOut must be after checkIn");
    }
}