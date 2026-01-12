package com.caco.sitedocaco.dto.response.store;

import java.util.UUID;

public record ProductCategoryDTO(
        UUID id,
        String name,
        String slug,
        Integer order
) {}
