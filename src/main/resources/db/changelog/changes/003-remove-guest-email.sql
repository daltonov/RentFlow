--liquibase formatted sql

--changeset rentflow:003-remove-guest-email
ALTER TABLE guests DROP COLUMN email;