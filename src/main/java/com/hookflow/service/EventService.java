package com.hookflow.service;

import com.hookflow.dto.EventRequest;
import com.hookflow.entity.Event;
import com.hookflow.repository.EventRepository;
import com.hookflow.service.DeliveryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final DeliveryService deliveryService;

    public EventService(
            EventRepository eventRepository,
            DeliveryService deliveryService) {
        this.eventRepository = eventRepository;
        this.deliveryService = deliveryService;
    }

    @Transactional
    public Map<String, Object> acceptEvent(EventRequest request) {

        Event event = new Event(
                UUID.randomUUID(),
                request.eventType(),
                request.source(),
                "ACCEPTED",
                request.data(),
                Instant.now()
        );

        Event savedEvent = eventRepository.save(event);

        int deliveriesCreated =
                deliveryService.createPendingDeliveries(savedEvent);

        return Map.of(
                "eventId", savedEvent.getId().toString(),
                "status", savedEvent.getStatus(),
                "eventType", savedEvent.getEventType(),
                "source", savedEvent.getSource(),
                "data", savedEvent.getData(),
                "createdAt", savedEvent.getCreatedAt().toString(),
                "deliveriesCreated", deliveriesCreated
        );
    }
}