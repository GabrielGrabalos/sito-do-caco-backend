package com.caco.sitedocaco.controller.admin;

import com.caco.sitedocaco.dto.request.event.CreateEventDTO;
import com.caco.sitedocaco.dto.request.event.UpdateEventDTO;
import com.caco.sitedocaco.dto.response.event.EventResponseDTO;
import com.caco.sitedocaco.entity.event.Event;
import com.caco.sitedocaco.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EventAdminController {

    private final EventService eventService;


    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(
            @Valid @RequestBody CreateEventDTO dto) {
        Event event = eventService.createEvent(dto);
        EventResponseDTO response = eventService.getEventById(event.getId(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventDTO dto) {
        Event event = eventService.updateEvent(eventId, dto);
        EventResponseDTO response = eventService.getEventById(event.getId(), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
    }
}