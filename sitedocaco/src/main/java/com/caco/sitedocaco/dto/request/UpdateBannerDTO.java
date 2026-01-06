package com.caco.sitedocaco.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateBannerDTO(
        String title,
        MultipartFile imageFile,
        String targetLink,
        Boolean active
) {}