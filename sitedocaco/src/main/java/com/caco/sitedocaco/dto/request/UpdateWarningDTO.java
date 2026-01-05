package com.caco.sitedocaco.dto.request;

import java.time.LocalDateTime;

public record UpdateWarningDTO(
        String markdownText,
        LocalDateTime startsAt,
        LocalDateTime expiresAt
) {}
