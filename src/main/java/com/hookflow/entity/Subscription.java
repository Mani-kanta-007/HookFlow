package com.hookflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String destinationName;

    @Column(nullable = false)
    private String endpointUrl;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Required by JPA
    protected Subscription() {
    }

    public Subscription(
            UUID id,
            String destinationName,
            String endpointUrl,
            String eventType,
            boolean active,
            Instant createdAt) {
        this.id = id;
        this.destinationName = destinationName;
        this.endpointUrl = endpointUrl;
        this.eventType = eventType;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}