package com.hookflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SubscriptionRequest(

        @NotBlank(message = "destinationName is required")
        String destinationName,

        @NotBlank(message = "endpointUrl is required")
        @Pattern(
                regexp = "^https?://.+",
                message = "endpointUrl must start with http:// or https://"
        )
        String endpointUrl,

        @NotBlank(message = "eventType is required")
        String eventType

) {}