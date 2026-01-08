package com.caco.sitedocaco.dto.request.manual;

import jakarta.validation.constraints.NotNull;

public record CreateArticleFeedbackDTO(
        @NotNull
        Boolean isHelpful,

        String comment
) {}