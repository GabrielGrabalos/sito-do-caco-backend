package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.ExamType;
import org.springframework.web.multipart.MultipartFile;

public record UpdateExamDTO(
        String subjectCode,
        Integer year,
        ExamType type,
        String fileUrl
) {}