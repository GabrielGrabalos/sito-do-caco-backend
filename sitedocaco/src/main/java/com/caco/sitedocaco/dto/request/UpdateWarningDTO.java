package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.SeverityLevel;

import java.time.LocalDateTime;

public record UpdateWarningDTO(
        String markdownText,
        SeverityLevel severityLevel,
        LocalDateTime startsAt,
        LocalDateTime expiresAt
) {}
