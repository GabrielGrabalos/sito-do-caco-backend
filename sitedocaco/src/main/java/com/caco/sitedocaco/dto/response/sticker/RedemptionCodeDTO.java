package com.caco.sitedocaco.dto.response.sticker;

import com.caco.sitedocaco.entity.sticker.RedemptionCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record RedemptionCodeDTO(
        String code,
        UUID stickerId,
        Boolean oneTimeUse,
        Boolean used,
        LocalDateTime expiresAt
) {
    public static RedemptionCodeDTO fromEntity(RedemptionCode entity) {
        return new RedemptionCodeDTO(
                entity.getCode(),
                entity.getSticker() != null ? entity.getSticker().getId() : null,
                entity.getIsOneTimeUse(),
                entity.getIsUsed(),
                entity.getExpiresAt()
        );
    }
}

