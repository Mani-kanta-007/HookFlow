package com.hookflow.repository;

import com.hookflow.entity.Delivery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository
        extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByEvent_Id(UUID eventId);

    @EntityGraph(attributePaths = {"event", "subscription"})
    List<Delivery> findByStatus(Delivery.Status status);
}