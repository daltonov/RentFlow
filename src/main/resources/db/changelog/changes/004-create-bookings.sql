--liquibase formatted sql

--changeset rentflow:004-create-bookings
CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    property_id UUID NOT NULL REFERENCES properties(id),
    guest_id UUID NOT NULL REFERENCES guests(id),
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    source VARCHAR(16) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_bookings_dates
        CHECK (check_out > check_in),

    CONSTRAINT chk_bookings_total_price
        CHECK (total_price >= 0),

    CONSTRAINT chk_bookings_status
        CHECK (status IN (
            'NEW', 'CONTACTED', 'WAITING_FOR_PAYMENT',
            'CONFIRMED', 'CHECK_IN_READY', 'CHECKED_IN',
            'CHECKED_OUT', 'COMPLETED', 'CANCELLED', 'EXPIRED'
        )),

    CONSTRAINT chk_bookings_source
        CHECK (source IN ('DIRECT', 'AVITO', 'SUTOCHNO'))
);

CREATE INDEX idx_bookings_property_dates
    ON bookings(property_id, check_in, check_out);

CREATE INDEX idx_bookings_guest
    ON bookings(guest_id);