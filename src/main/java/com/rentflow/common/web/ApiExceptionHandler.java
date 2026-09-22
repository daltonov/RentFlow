package com.rentflow.common.web;

import com.rentflow.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.rentflow.common.exception.InvalidDateRangeException;
import com.rentflow.common.exception.BookingConflictException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BookingConflictException.class)
    public ProblemDetail handleBookingConflict(
            BookingConflictException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ProblemDetail handleInvalidDateRange(
            InvalidDateRangeException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        for (var error : exception.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        problem.setProperty("errors", errors);

        return problem;
    }
}