--liquibase formatted sql

--changeset rentflow:002-create-guests
CREATE TABLE guests (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    email VARCHAR(254),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);