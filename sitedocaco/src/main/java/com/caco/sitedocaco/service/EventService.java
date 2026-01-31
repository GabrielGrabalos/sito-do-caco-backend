package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.event.CreateEventDTO;
import com.caco.sitedocaco.dto.request.event.UpdateEventDTO;
import com.caco.sitedocaco.dto.response.event.*;
import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.event.Event;
import com.caco.sitedocaco.entity.event.EventGalleryItem;
import com.caco.sitedocaco.entity.event.UserEvent;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.EventGalleryItemRepository;
import com.caco.sitedocaco.repository.EventRepository;
import com.caco.sitedocaco.repository.UserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventGalleryItemRepository galleryItemRepository;
    private final UserEventRepository userEventRepository;
    private final UserService userService;
    private final ImgBBService imgBBService;

    // ========== MÉTODOS PÚBLICOS ==========

    @Transactional(readOnly = true)
    public Page<EventSummaryDTO> getUpcomingEvents(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<Event> events = eventRepository.findUpcomingEvents(now, pageable);
        return events.map(EventSummaryDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<EventSummaryDTO> getPastEvents(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<Event> events = eventRepository.findPastEvents(now, pageable);
        return events.map(EventSummaryDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        // Buscar status do usuário, se autenticado
        UserEvent.ParticipationStatus userStatus = null;
        if (userId != null) {
            Optional<UserEvent> userEvent = userEventRepository.findByUserAndEvent(
                    userService.getUserById(userId),
                    event
            );
            userStatus = userEvent.map(UserEvent::getStatus).orElse(null);
        }

        // Buscar galeria
        List<EventGalleryItem> galleryItems = galleryItemRepository.findByEventIdOrderByIdAsc(eventId);
        List<EventGalleryItemDTO> gallery = galleryItems.stream()
                .map(EventGalleryItemDTO::fromEntity)
                .toList();

        return EventResponseDTO.fromEntity(event, gallery, userStatus);
    }

    // ========== MÉTODOS PRIVADOS ==========

    @Transactional(readOnly = true)
    public Page<EventSummaryDTO> getUserSavedEvents(UUID userId, Pageable pageable) {
        User user = userService.getUserById(userId);

        // Usar o método com paginação e ordenação correta
        Page<UserEvent> userEvents = userEventRepository.findByUserOrderBySavedAtDesc(user, pageable);

        // Converter para EventSummaryDTO
        return userEvents.map(userEvent -> EventSummaryDTO.fromEntity(userEvent.getEvent()));
    }

    @Transactional(readOnly = true)
    public UserEvent getUserEventDetails(UUID eventId, UUID userId) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        return userEventRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new ResourceNotFoundException("Participação não encontrada"));
    }

    @Transactional
    public void saveEventForUser(UUID eventId, UUID userId, UserEvent.ParticipationStatus status) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        Optional<UserEvent> existing = userEventRepository.findByUserAndEvent(user, event);

        if (existing.isPresent()) {
            UserEvent userEvent = existing.get();
            userEvent.setStatus(status);
            userEventRepository.save(userEvent);
        } else {
            UserEvent userEvent = new UserEvent();
            userEvent.setUser(user);
            userEvent.setEvent(event);
            userEvent.setStatus(status);
            userEventRepository.save(userEvent);
        }
    }

    @Transactional
    public void unsaveEventForUser(UUID eventId, UUID userId) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        userEventRepository.deleteByUserAndEvent(user, event);
    }

    @Transactional
    public void updateParticipationStatus(UUID eventId, UUID userId, UserEvent.ParticipationStatus status) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        UserEvent userEvent = userEventRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new ResourceNotFoundException("Participação não encontrada"));

        userEvent.setStatus(status);
        userEventRepository.save(userEvent);
    }

    // ========== MÉTODOS ADMIN ==========

    @Transactional
    public Event createEvent(CreateEventDTO dto) throws IOException {
        validateEventDates(dto.startDate(), dto.endDate());

        Event event = new Event();
        event.setTitle(dto.title());
        event.setDescription(dto.description());
        event.setStartDate(dto.startDate());
        event.setEndDate(dto.endDate());
        event.setLocation(dto.location());

        String coverImageUrl = null;
        if( dto.coverImage() != null)
            coverImageUrl = imgBBService.uploadImage(dto.coverImage());

        event.setCoverImage(coverImageUrl);
        event.setType(dto.type());
        event.setImportance(dto.importance());
        event.setStatus(Event.EventStatus.SCHEDULED);

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(UUID eventId, UpdateEventDTO dto) throws IOException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        if (dto.startDate() != null && dto.endDate() != null) {
            validateEventDates(dto.startDate(), dto.endDate());
        }

        if (dto.title() != null) event.setTitle(dto.title());
        if (dto.slug() != null) event.setSlug(dto.slug());
        if (dto.description() != null) event.setDescription(dto.description());
        if (dto.startDate() != null) event.setStartDate(dto.startDate());
        if (dto.endDate() != null) event.setEndDate(dto.endDate());
        if (dto.location() != null) event.setLocation(dto.location());
        if (dto.removeCoverImage() != null && dto.removeCoverImage()) event.setCoverImage(null);
        if (dto.coverImage() != null) {
            String coverImageUrl = imgBBService.uploadImage(dto.coverImage());
            event.setCoverImage(coverImageUrl);
        }
        if (dto.type() != null) event.setType(dto.type());
        if (dto.importance() != null) event.setImportance(dto.importance());
        if (dto.status() != null) event.setStatus(dto.status());

        if (dto.galleryImages() != null) {
            galleryItemRepository.deleteByEventId(eventId);
            for (String imageUrl : dto.galleryImages()) {
                EventGalleryItem galleryItem = new EventGalleryItem();
                galleryItem.setEvent(event);
                galleryItem.setMediaUrl(imageUrl);
                galleryItem.setType(EventGalleryItem.MediaType.IMAGE);
                galleryItemRepository.save(galleryItem);
            }
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento não encontrado");
        }

        eventRepository.deleteById(eventId);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private void validateEventDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("A data de término deve ser após a data de início");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateEventStatuses() {
        LocalDateTime now = LocalDateTime.now();

        List<Event> allEvents = eventRepository.findAll();
        for (Event event : allEvents) {
            Event.EventStatus newStatus = calculateEventStatus(event, now);
            if (event.getStatus() != newStatus) {
                event.setStatus(newStatus);
                eventRepository.save(event);
            }
        }
    }

    private Event.EventStatus calculateEventStatus(Event event, LocalDateTime now) {
        if (event.getStartDate().isAfter(now)) {
            return Event.EventStatus.SCHEDULED;
        } else if (event.getEndDate().isAfter(now)) {
            return Event.EventStatus.HAPPENING;
        } else {
            return Event.EventStatus.ENDED;
        }
    }

    public EventResponseDTO getEventBySlug(String slug, UUID userId) {
        Event event = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        // Buscar status do usuário, se autenticado
        UserEvent.ParticipationStatus userStatus = null;
        if (userId != null) {
            Optional<UserEvent> userEvent = userEventRepository.findByUserAndEvent(
                    userService.getUserById(userId),
                    event
            );
            userStatus = userEvent.map(UserEvent::getStatus).orElse(null);
        }

        // Buscar galeria
        List<EventGalleryItem> galleryItems = galleryItemRepository.findByEventIdOrderByIdAsc(event.getId());
        List<EventGalleryItemDTO> gallery = galleryItems.stream()
                .map(EventGalleryItemDTO::fromEntity)
                .toList();

        return EventResponseDTO.fromEntity(event, gallery, userStatus);
    }
}