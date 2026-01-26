package com.caco.sitedocaco.controller.publicController;

import com.caco.sitedocaco.dto.response.event.EventResponseDTO;
import com.caco.sitedocaco.dto.response.event.EventSummaryDTO;
import com.caco.sitedocaco.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventSummaryDTO>> getUpcomingEvents(
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EventSummaryDTO> events = eventService.getUpcomingEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/past")
    public ResponseEntity<Page<EventSummaryDTO>> getPastEvents(
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EventSummaryDTO> events = eventService.getPastEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable UUID eventId,
            @RequestAttribute(value = "userId", required = false) UUID userId) {
        EventResponseDTO event = eventService.getEventById(eventId, userId);
        return ResponseEntity.ok(event);
    }
}