package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.SeverityLevel;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateWarningDTO(
        @NotBlank String markdownText,
        SeverityLevel severityLevel,
        @NotNull LocalDateTime startsAt,
        @NotNull @Future LocalDateTime expiresAt
) {}
