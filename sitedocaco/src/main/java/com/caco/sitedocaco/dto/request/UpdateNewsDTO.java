package com.caco.sitedocaco.dto.request;

public record UpdateNewsDTO(
        String title,
        String summary,
        String content,
        String coverImage
) {}
