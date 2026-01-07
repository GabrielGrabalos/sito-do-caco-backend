package com.caco.sitedocaco.dto.response;

import com.caco.sitedocaco.entity.enums.ExamType;

import java.util.UUID;

public record ExamWithoutSubjectDTO(
        UUID id,
        Integer year,
        ExamType type,
        String fileUrl
) {
}
