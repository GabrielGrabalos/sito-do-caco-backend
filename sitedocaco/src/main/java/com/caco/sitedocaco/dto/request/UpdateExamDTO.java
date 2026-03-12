package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.ExamType;

import java.util.UUID;

public record UpdateExamDTO(
        String subjectCode,
        UUID professorId,
        boolean removeProfessor,
        Integer year,
        ExamType type,
        String fileUrl
) {}