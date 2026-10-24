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

        private static final int MAX_ATTEMPTS = 5;
        private static final long INITIAL_DELAY_SECONDS = 5;
        private static final long MAX_DELAY_SECONDS = 60;

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

        @Transactional
        public void recordSuccess(UUID deliveryId) {
                Delivery delivery = deliveryRepository.findById(deliveryId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Delivery not found: " + deliveryId));

                delivery.recordSuccess();
        }

        @Transactional
        public void recordFailure(UUID deliveryId, String error) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Delivery not found: " + deliveryId));

        int nextAttemptNumber = delivery.getAttemptCount() + 1;
        boolean permanentlyFailed = nextAttemptNumber >= MAX_ATTEMPTS;

        long delaySeconds = Math.min(
                INITIAL_DELAY_SECONDS * (1L << (nextAttemptNumber - 1)),
                MAX_DELAY_SECONDS
        );

        Instant retryAt = permanentlyFailed
                ? null
                : Instant.now().plusSeconds(delaySeconds);

        delivery.recordFailure(error, retryAt, permanentlyFailed);
        }

}