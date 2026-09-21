package com.rentflow.common.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String resource, UUID id) {
        super(resource + " with id " + id + " was not found");
    }
}