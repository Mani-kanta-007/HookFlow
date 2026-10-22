package com.hookflow.controller;

import com.hookflow.dto.SubscriptionRequest;
import com.hookflow.dto.SubscriptionResponse;
import com.hookflow.service.SubscriptionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {

        return subscriptionService.createSubscription(request);
    }

    @GetMapping
    public List<SubscriptionResponse> getSubscriptions(
            @RequestParam(required = false) String eventType) {

        return subscriptionService.getSubscriptions(eventType);
    }
}