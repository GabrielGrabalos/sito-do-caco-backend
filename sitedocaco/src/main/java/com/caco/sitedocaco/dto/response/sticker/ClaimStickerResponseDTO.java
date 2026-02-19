package com.caco.sitedocaco.dto.response.sticker;

import java.time.LocalDateTime;

public record ClaimStickerResponseDTO(
        StickerPublicDTO sticker,
        LocalDateTime obtainedAt
) {
}

