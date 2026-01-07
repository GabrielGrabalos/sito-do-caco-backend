package com.caco.sitedocaco.entity.enums;

import lombok.Getter;

@Getter
public enum ImageType {
    PROFILE_AVATAR("avatar", 1, 1, 1024, 1024, 2 * 1024 * 1024), // 2MB max
    BANNER_IMAGE("banner", 21, 9, 1920, 822, 8 * 1024 * 1024), // 8MB max
    EVENT_COVER("event", 4, 3, 1200, 900, 8 * 1024 * 1024), // 8MB max
    NEWS_COVER("news", 3, 2, 1200, 800, 5 * 1024 * 1024); // 5MB max

    private final String folderName;
    private final int aspectRatioWidth;
    private final int aspectRatioHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final long maxSizeBytes; // Limite em bytes

    ImageType(String folderName, int aspectRatioWidth, int aspectRatioHeight,
              int maxWidth, int maxHeight, long maxSizeBytes) {
        this.folderName = folderName;
        this.aspectRatioWidth = aspectRatioWidth;
        this.aspectRatioHeight = aspectRatioHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxSizeBytes = maxSizeBytes;
    }

    public double getAspectRatio() {
        return (double) aspectRatioWidth / aspectRatioHeight;
    }

    public String getValidationErrorMessage() {
        return String.format(
                "Imagem deve ter aspect ratio %d:%d e tamanho máximo de %dMB",
                aspectRatioWidth, aspectRatioHeight, maxSizeBytes / (1024 * 1024)
        );
    }
}