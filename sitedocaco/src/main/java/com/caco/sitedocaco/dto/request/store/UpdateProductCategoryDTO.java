package com.caco.sitedocaco.dto.request.store;

import jakarta.validation.constraints.Pattern;

public record UpdateProductCategoryDTO(
        String name,

        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug deve conter apenas letras minúsculas, números e hífens")
        String slug
) {}