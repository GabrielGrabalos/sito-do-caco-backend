package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.event.SaveEventRequestDTO;
import com.caco.sitedocaco.dto.response.event.EventSummaryDTO;
import com.caco.sitedocaco.entity.event.UserEvent;
import com.caco.sitedocaco.service.EventService;
import com.caco.sitedocaco.service.UserService;
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
    private final UserService userService;

    @PostMapping("/{eventId}/save")
    public ResponseEntity<Void> saveEvent(
            @PathVariable UUID eventId,
            @RequestBody SaveEventRequestDTO dto) {
        eventService.saveEventForUser(eventId, userService.getCurrentUser().getId(), dto.status());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}/save")
    public ResponseEntity<Void> unsaveEvent(
            @PathVariable UUID eventId) {
        eventService.unsaveEventForUser(eventId, userService.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<Page<EventSummaryDTO>> getSavedEvents(
            @PageableDefault(size = 10, sort = "savedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EventSummaryDTO> events = eventService.getUserSavedEvents(userService.getCurrentUser().getId(), pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/details")
    public ResponseEntity<UserEvent> getUserEventDetails(
            @PathVariable UUID eventId) {
        UserEvent userEvent = eventService.getUserEventDetails(eventId, userService.getCurrentUser().getId());
        return ResponseEntity.ok(userEvent);
    }

    @PutMapping("/{eventId}/status")
    public ResponseEntity<Void> updateParticipationStatus(
            @PathVariable UUID eventId,
            @RequestParam UserEvent.ParticipationStatus status) {
        eventService.updateParticipationStatus(eventId, userService.getCurrentUser().getId(), status);
        return ResponseEntity.ok().build();
    }
}