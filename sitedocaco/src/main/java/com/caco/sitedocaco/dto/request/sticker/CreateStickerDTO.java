package com.caco.sitedocaco.dto.request.sticker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateStickerDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String name,

        @Size(max = 600, message = "Descrição deve ter no máximo 600 caracteres")
        String description,

        UUID originEventId
) {
}
