package com.caco.sitedocaco.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record WarningDTO(
        UUID id,
        String markdownText,
        LocalDateTime expiresAt
) {}