package com.caco.sitedocaco.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewsSummaryDTO(
        UUID id,
        String title,
        String slug,
        String summary,
        String coverImage,
        LocalDateTime publishDate,
        String authorName
) {}