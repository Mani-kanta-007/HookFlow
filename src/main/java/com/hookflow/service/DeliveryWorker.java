
package com.hookflow.service;

import com.hookflow.entity.Delivery;
import com.hookflow.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class DeliveryWorker {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;
    private final RestClient restClient;

    private static final Set<Integer> ALLOWED_PORTS =
            Set.of(8080, 9001, 9002);

    public DeliveryWorker(
            DeliveryRepository deliveryRepository,
            DeliveryService deliveryService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryService = deliveryService;
        this.restClient = RestClient.create();
    }

    public Map<String, Object> processPendingDeliveries() {
        List<Delivery> deliveries =
                deliveryRepository.findByStatus(Delivery.Status.PENDING);

        int delivered = 0;
        int failed = 0;

        for (Delivery delivery : deliveries) {
            try {
                String endpointUrl =
                        delivery.getSubscription().getEndpointUrl();

                validateEndpoint(endpointUrl);

                Map<String, Object> payload = Map.of(
                        "eventId", delivery.getEvent().getId().toString(),
                        "eventType", delivery.getEvent().getEventType(),
                        "source", delivery.getEvent().getSource(),
                        "data", delivery.getEvent().getData()
                );

                restClient.post()
                        .uri(endpointUrl)
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();

                deliveryService.recordAttempt(
                        delivery.getId(), Delivery.Status.DELIVERED);

                delivered++;

            } catch (Exception exception) {
                try {
                    deliveryService.recordAttempt(
                            delivery.getId(), Delivery.Status.FAILED);
                } catch (Exception statusException) {
                    exception.addSuppressed(statusException);
                }

                failed++;

                System.err.println(
                        "Delivery failed: " + delivery.getId()
                                + " - " + exception.getMessage());
            }
        }

        return Map.of(
                "pendingFound", deliveries.size(),
                "processed", delivered + failed,
                "delivered", delivered,
                "failed", failed
        );
    }

    private void validateEndpoint(String endpointUrl) {
        URI uri;

        try {
            uri = URI.create(endpointUrl);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid webhook URL");
        }

        if (!"http".equalsIgnoreCase(uri.getScheme())
                || !"localhost".equalsIgnoreCase(uri.getHost())
                || !ALLOWED_PORTS.contains(uri.getPort())
                || !"/webhooks/orders".equals(uri.getPath())
                || uri.getUserInfo() != null
                || uri.getQuery() != null
                || uri.getFragment() != null) {
            throw new IllegalArgumentException(
                    "Webhook endpoint is not allowed: " + endpointUrl);
        }
    }
}
