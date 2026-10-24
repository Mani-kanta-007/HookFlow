package com.hookflow.repository;

import com.hookflow.entity.Delivery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import java.time.Instant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryRepository
        extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByEvent_Id(UUID eventId);

    @EntityGraph(attributePaths = {"event", "subscription"})
    List<Delivery> findByStatus(Delivery.Status status);

    @EntityGraph(attributePaths = {"event", "subscription"})
    @Query("""
        SELECT d FROM Delivery d
        WHERE d.status = :status
        AND (d.nextAttemptAt IS NULL OR d.nextAttemptAt <= :now)
        """)
    List<Delivery> findEligibleDeliveries(
            @Param("status") Delivery.Status status,
            @Param("now") Instant now);
}