package com.caco.sitedocaco.dto.request.manual;

import java.util.UUID;

public record UpdateManualChapterDTO(
        String title,
        String slug,
        UUID categoryId
) {}