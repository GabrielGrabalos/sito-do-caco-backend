package com.caco.sitedocaco.dto.response.store;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDetailDTO(
        UUID id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        boolean manageStock,
        boolean outOfStock,
        UUID categoryId,
        String categoryName,
        String categorySlug,
        List<String> images,
        List<ProductVariationDTO> variations
) {}