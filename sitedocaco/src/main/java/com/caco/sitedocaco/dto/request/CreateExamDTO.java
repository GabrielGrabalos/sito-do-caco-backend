// main/java/com/caco/sitedocaco/dto/request/CreateExamDTO.java
package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.ExamType;
import jakarta.validation.constraints.NotNull;

public record CreateExamDTO(
        @NotNull(message = "A disciplina é obrigatória")
        String subjectCode,

        @NotNull(message = "O ano é obrigatório")
        Integer year,

        @NotNull(message = "O tipo de prova é obrigatório")
        ExamType type,

        @NotNull(message = "O link do PDF é obrigatório")
        String fileUrl
) {}