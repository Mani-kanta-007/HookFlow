package com.hookflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
public class Delivery {

    public enum Status {
        PENDING,
        DELIVERED,
        FAILED
    }

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private int attemptCount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant nextAttemptAt;

    @Column(length = 2000)
    private String lastError;

    protected Delivery() {
    }

    public Delivery(
            UUID id,
            Event event,
            Subscription subscription,
            Status status,
            int attemptCount,
            Instant createdAt) {
        this.id = id;
        this.event = event;
        this.subscription = subscription;
        this.status = status;
        this.attemptCount = attemptCount;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public Status getStatus() {
        return status;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    

    public Instant getNextAttemptAt() {
        return nextAttemptAt;
    }

    public String getLastError() {
        return lastError;
    }


    public void recordSuccess() {
        this.attemptCount++;
        this.status = Status.DELIVERED;
        this.nextAttemptAt = null;
        this.lastError = null;
    }

    public void recordFailure(
            String error,
            Instant retryAt,
            boolean permanentlyFailed) {

        this.attemptCount++;
        this.status = permanentlyFailed ? Status.FAILED : Status.PENDING;
        this.nextAttemptAt = permanentlyFailed ? null : retryAt;
        this.lastError = error;
    }
}