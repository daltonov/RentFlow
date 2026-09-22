package com.rentflow.guest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 32)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Guest() {
    }

    public Guest(String firstName, String lastName,
                 String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    @PrePersist
    private void beforeInsert() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}