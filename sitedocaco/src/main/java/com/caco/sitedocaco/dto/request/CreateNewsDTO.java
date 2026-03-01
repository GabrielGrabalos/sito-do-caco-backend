package com.caco.sitedocaco.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record CreateNewsDTO(
        @NotBlank(message = "O título é obrigatório") String title,
        @NotBlank(message = "O resumo é obrigatório") String summary,
        @NotBlank(message = "O conteúdo é obrigatório") String content,
        MultipartFile coverImage // Opcional
) {}