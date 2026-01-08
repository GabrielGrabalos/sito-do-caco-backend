package com.caco.sitedocaco.dto.request.manual;

import jakarta.validation.constraints.NotBlank;

public record CreateManualCategoryDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "O slug é obrigatório")
        String slug
) {}