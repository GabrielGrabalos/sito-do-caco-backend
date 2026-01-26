package com.caco.sitedocaco.dto.response.event;

import com.caco.sitedocaco.entity.event.EventGalleryItem;

import java.util.UUID;

public record EventGalleryItemDTO(
        UUID id,
        String mediaUrl,
        EventGalleryItem.MediaType type
) {
    public static EventGalleryItemDTO fromEntity(EventGalleryItem item) {
        return new EventGalleryItemDTO(
                item.getId(),
                item.getMediaUrl(),
                item.getType()
        );
    }
}