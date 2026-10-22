package com.hookflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> data;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Required by JPA
    protected Event() {
    }

    public Event(
            UUID id,
            String eventType,
            String source,
            String status,
            Map<String, Object> data,
            Instant createdAt) {
        this.id = id;
        this.eventType = eventType;
        this.source = source;
        this.status = status;
        this.data = data;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}