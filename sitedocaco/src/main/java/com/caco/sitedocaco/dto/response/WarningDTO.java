package com.caco.sitedocaco.dto.response;

import com.caco.sitedocaco.entity.enums.SeverityLevel;

import java.time.LocalDateTime;
import java.util.UUID;

public record WarningDTO(
        UUID id,
        String markdownText,
        SeverityLevel severityLevel,
        LocalDateTime startsAt,
        LocalDateTime expiresAt
) {}