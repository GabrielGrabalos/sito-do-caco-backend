package com.caco.sitedocaco.dto.request.event;

import com.caco.sitedocaco.entity.event.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateEventDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "A data de início é obrigatória")
        LocalDateTime startDate,

        @NotNull(message = "A data de término é obrigatória")
        LocalDateTime endDate,

        String location,

        String coverImage,

        @NotNull(message = "O tipo é obrigatório")
        Event.EventType type,

        @NotNull(message = "A importância é obrigatória")
        Event.EventImportance importance,

        List<String> galleryImages
) {}