package com.caco.sitedocaco.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBannerDTO(
        @NotBlank String title,
        @NotBlank String imageUrl,
        String targetLink,
        Boolean active
) {}
