package com.caco.sitedocaco.dto.request.event;

import com.caco.sitedocaco.entity.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateEventDTO(
        String title,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        String coverImage,
        Event.EventType type,
        Event.EventImportance importance,
        Event.EventStatus status,
        List<String> galleryImages
) {}