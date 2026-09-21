--liquibase formatted sql

--changeset rentflow:001-create-properties
CREATE TABLE properties (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500) NOT NULL,
    max_guests INTEGER NOT NULL,
    default_price NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_properties_max_guests CHECK (max_guests > 0),
    CONSTRAINT chk_properties_default_price CHECK (default_price >= 0)
);