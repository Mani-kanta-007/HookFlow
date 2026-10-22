package com.hookflow.service;

import com.hookflow.entity.Delivery;
import com.hookflow.entity.Event;
import com.hookflow.entity.Subscription;
import com.hookflow.repository.DeliveryRepository;
import com.hookflow.repository.SubscriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {

    private final SubscriptionRepository subscriptionRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(
            SubscriptionRepository subscriptionRepository,
            DeliveryRepository deliveryRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public int createPendingDeliveries(Event event) {

        List<Subscription> subscriptions =
                subscriptionRepository
                        .findByEventTypeAndActiveTrue(event.getEventType());

        List<Delivery> deliveries = subscriptions.stream()
                .map(subscription -> new Delivery(
                        UUID.randomUUID(),
                        event,
                        subscription,
                        Delivery.Status.PENDING,
                        0,
                        Instant.now()
                ))
                .toList();

        deliveryRepository.saveAll(deliveries);

        return deliveries.size();
    }
}