package com.caco.sitedocaco.dto.request.manual;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ReorderChaptersDTO(
        @NotNull
        UUID categoryId,

        @NotNull
        List<UUID> chapterIds
) {}