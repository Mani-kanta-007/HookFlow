package com.hookflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks")
public class TestReceiverController {

    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> receiveOrder(
            @RequestBody Map<String, Object> payload) {

        System.out.println("Webhook received: " + payload);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Webhook received successfully",
                        "received", true
                )
        );
    }
}