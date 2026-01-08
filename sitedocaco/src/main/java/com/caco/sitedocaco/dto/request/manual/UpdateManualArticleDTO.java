package com.caco.sitedocaco.dto.request.manual;

import java.util.UUID;

public record UpdateManualArticleDTO(
        String title,
        String slug,
        String content,
        UUID chapterId
) {}