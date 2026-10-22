package com.hookflow.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String destinationName,
        String endpointUrl,
        String eventType,
        boolean active,
        Instant createdAt
) {}