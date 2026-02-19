package com.caco.sitedocaco.dto.response.sticker;

import com.caco.sitedocaco.entity.sticker.UserSticker;

import java.time.LocalDateTime;

public record MyStickerDTO(
        StickerPublicDTO sticker,
        LocalDateTime obtainedAt
) {
    public static MyStickerDTO fromEntity(UserSticker userSticker) {
        return new MyStickerDTO(
                StickerPublicDTO.fromEntity(userSticker.getSticker()),
                userSticker.getObtainedAt()
        );
    }
}

