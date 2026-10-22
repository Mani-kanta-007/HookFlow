
package com.hookflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record EventRequest(

        @NotBlank(message = "eventType is required")
        String eventType,

        @NotBlank(message = "source is required")
        String source,

        @NotNull(message = "data is required")
        Map<String, Object> data

) {}
