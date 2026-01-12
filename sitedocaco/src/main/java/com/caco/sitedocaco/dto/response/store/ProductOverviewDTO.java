package com.caco.sitedocaco.dto.response.store;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductOverviewDTO(
        UUID id,
        String name,
        String slug,
        BigDecimal price,
        BigDecimal originalPrice,
        String coverImage,
        boolean outOfStock,
        UUID categoryId,
        String categoryName,
        String categorySlug,
        LocalDateTime createdAt
) {}