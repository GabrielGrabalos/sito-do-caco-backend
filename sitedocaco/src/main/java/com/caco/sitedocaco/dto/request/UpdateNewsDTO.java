package com.caco.sitedocaco.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateNewsDTO(
        String title,
        String summary,
        String content,
        MultipartFile coverImage,       // nova imagem (opcional)
        Boolean removeCoverImage        // true = remove a imagem atual sem substituir
) {}
