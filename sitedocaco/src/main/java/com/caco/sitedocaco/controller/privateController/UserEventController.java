package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.event.SaveEventRequestDTO;
import com.caco.sitedocaco.dto.response.event.EventSummaryDTO;
import com.caco.sitedocaco.entity.event.UserEvent;
import com.caco.sitedocaco.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/private/user/events")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserEventController {

    private final EventService eventService;

    @PostMapping("/{eventId}/save")
    public ResponseEntity<Void> saveEvent(
            @PathVariable UUID eventId,
            @RequestBody SaveEventRequestDTO dto,
            @RequestAttribute("userId") UUID userId) {
        eventService.saveEventForUser(eventId, userId, dto.status());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}/save")
    public ResponseEntity<Void> unsaveEvent(
            @PathVariable UUID eventId,
            @RequestAttribute("userId") UUID userId) {
        eventService.unsaveEventForUser(eventId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<Page<EventSummaryDTO>> getSavedEvents(
            @RequestAttribute("userId") UUID userId,
            @PageableDefault(size = 10, sort = "savedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EventSummaryDTO> events = eventService.getUserSavedEvents(userId, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/details")
    public ResponseEntity<UserEvent> getUserEventDetails(
            @PathVariable UUID eventId,
            @RequestAttribute("userId") UUID userId) {
        UserEvent userEvent = eventService.getUserEventDetails(eventId, userId);
        return ResponseEntity.ok(userEvent);
    }

    @PutMapping("/{eventId}/status")
    public ResponseEntity<Void> updateParticipationStatus(
            @PathVariable UUID eventId,
            @RequestParam UserEvent.ParticipationStatus status,
            @RequestAttribute("userId") UUID userId) {
        eventService.updateParticipationStatus(eventId, userId, status);
        return ResponseEntity.ok().build();
    }
}