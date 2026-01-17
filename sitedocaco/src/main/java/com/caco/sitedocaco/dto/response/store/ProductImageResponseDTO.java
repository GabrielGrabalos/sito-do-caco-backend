package com.caco.sitedocaco.dto.response.store;

import java.util.UUID;

public record ProductImageResponseDTO(
        UUID id,
        String imageUrl,
        Integer displayOrder,
        UUID productId
) {}