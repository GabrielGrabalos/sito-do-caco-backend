package com.caco.sitedocaco.dto.response.manual;

import com.caco.sitedocaco.entity.manual.ManualCategory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ManualCategoryDTO(
        UUID id,
        String title,
        String slug,
        Integer order,

        @JsonProperty("chapterCount")
        Long chapterCount
) {
    public static ManualCategoryDTO fromEntity(ManualCategory category, Long chapterCount) {
        return new ManualCategoryDTO(
                category.getId(),
                category.getTitle(),
                category.getSlug(),
                category.getOrder(),
                chapterCount
        );
    }
}