package com.caco.sitedocaco.dto.response.sticker;

import com.caco.sitedocaco.entity.sticker.Sticker;

import java.time.LocalDateTime;
import java.util.UUID;

public record StickerPublicDTO(
        UUID id,
        String name,
        String description,
        String imageUrl,
        UUID originEventId,
        LocalDateTime createdAt
) {
    public static StickerPublicDTO fromEntity(Sticker sticker) {
        return new StickerPublicDTO(
                sticker.getId(),
                sticker.getName(),
                sticker.getDescription(),
                sticker.getImageUrl(),
                sticker.getOriginEvent() != null ? sticker.getOriginEvent().getId() : null,
                sticker.getCreatedAt()
        );
    }
}

