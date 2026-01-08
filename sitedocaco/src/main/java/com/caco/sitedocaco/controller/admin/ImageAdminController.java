package com.caco.sitedocaco.controller.admin;

import com.caco.sitedocaco.dto.response.ImageUploadResponseDTO;
import com.caco.sitedocaco.entity.enums.ImageType;
import com.caco.sitedocaco.service.ImgBBService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/images")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ImageAdminController {

    private final ImgBBService imgBBService;

    /**
     * Upload de imagem genérica
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponseDTO> uploadImage(
            @RequestParam("image") @NotNull MultipartFile image) throws IOException {
        String imageUrl = imgBBService.uploadImage(image);

        return ResponseEntity.ok(new ImageUploadResponseDTO(imageUrl));
    }
}