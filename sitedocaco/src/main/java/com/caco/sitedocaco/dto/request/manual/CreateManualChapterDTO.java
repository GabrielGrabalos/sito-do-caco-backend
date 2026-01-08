package com.caco.sitedocaco.dto.request.manual;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateManualChapterDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "O slug é obrigatório")
        String slug,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoryId
) {}