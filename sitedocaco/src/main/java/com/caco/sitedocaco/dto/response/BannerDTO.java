package com.caco.sitedocaco.dto.response;

import java.util.UUID;

public record BannerDTO(
        UUID id,
        String title,
        String imageUrl,
        String targetLink
) {}