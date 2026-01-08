package com.caco.sitedocaco.dto.response.manual;

import com.caco.sitedocaco.entity.manual.ManualArticle;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ManualArticleDTO(
        UUID id,
        String title,
        String slug,
        String content,
        Integer order,
        UUID chapterId,
        String chapterTitle,
        UUID categoryId,
        String categoryTitle,

        @JsonProperty("helpfulCount")
        Long helpfulCount,

        @JsonProperty("unhelpfulCount")
        Long unhelpfulCount
) {
    public static ManualArticleDTO fromEntity(ManualArticle article, Long helpfulCount, Long unhelpfulCount) {
        return new ManualArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getContent(),
                article.getOrder(),
                article.getChapter().getId(),
                article.getChapter().getTitle(),
                article.getChapter().getCategory().getId(),
                article.getChapter().getCategory().getTitle(),
                helpfulCount,
                unhelpfulCount
        );
    }
}