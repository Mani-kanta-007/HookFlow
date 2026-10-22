package com.hookflow.controller;

import com.hookflow.service.DeliveryWorker;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryWorker deliveryWorker;

    public DeliveryController(DeliveryWorker deliveryWorker) {
        this.deliveryWorker = deliveryWorker;
    }

    @PostMapping("/process-pending")
    public Map<String, Object> processPending() {
        return deliveryWorker.processPendingDeliveries();
    }
}