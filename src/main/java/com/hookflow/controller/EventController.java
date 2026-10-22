
package com.hookflow.controller;

import com.hookflow.dto.EventRequest;
import com.hookflow.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> receiveEvent(
            @Valid @RequestBody EventRequest event) {

        return eventService.acceptEvent(event);
    }
}
