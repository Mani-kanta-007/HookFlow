package com.hookflow.service;

import com.hookflow.dto.SubscriptionRequest;
import com.hookflow.dto.SubscriptionResponse;
import com.hookflow.entity.Subscription;
import com.hookflow.repository.SubscriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public SubscriptionResponse createSubscription(
            SubscriptionRequest request) {

        Subscription subscription = new Subscription(
                UUID.randomUUID(),
                request.destinationName(),
                request.endpointUrl(),
                request.eventType(),
                true,
                Instant.now()
        );

        Subscription saved =
                subscriptionRepository.save(subscription);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptions(
            String eventType) {

        List<Subscription> subscriptions;

        if (eventType == null || eventType.isBlank()) {
            subscriptions = subscriptionRepository.findAll();
        } else {
            subscriptions =
                    subscriptionRepository.findByEventType(eventType);
        }

        return subscriptions.stream()
                .map(this::toResponse)
                .toList();
    }

    private SubscriptionResponse toResponse(
            Subscription subscription) {

        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getDestinationName(),
                subscription.getEndpointUrl(),
                subscription.getEventType(),
                subscription.isActive(),
                subscription.getCreatedAt()
        );
    }
}