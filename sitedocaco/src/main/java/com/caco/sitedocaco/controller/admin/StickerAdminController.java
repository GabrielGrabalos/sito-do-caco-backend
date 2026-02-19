package com.caco.sitedocaco.controller.admin;

import com.caco.sitedocaco.dto.request.sticker.CreateStickerDTO;
import com.caco.sitedocaco.dto.request.sticker.GenerateRedemptionCodesDTO;
import com.caco.sitedocaco.dto.response.sticker.RedemptionCodeBatchResponseDTO;
import com.caco.sitedocaco.dto.response.sticker.StickerAdminDTO;
import com.caco.sitedocaco.service.RedemptionCodeService;
import com.caco.sitedocaco.service.StickerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/stickers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StickerAdminController {

    private final StickerService stickerService;
    private final RedemptionCodeService redemptionCodeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StickerAdminDTO> create(
            @RequestPart("dto") @Valid CreateStickerDTO dto,
            @RequestPart("image") @Valid MultipartFile imageFile
    ) throws IOException {
        StickerAdminDTO created = stickerService.createSticker(dto, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{stickerId}/codes")
    public ResponseEntity<RedemptionCodeBatchResponseDTO> generateCodes(
            @PathVariable UUID stickerId,
            @RequestBody @Valid GenerateRedemptionCodesDTO dto
    ) {
        RedemptionCodeBatchResponseDTO resp = redemptionCodeService.generateBatch(stickerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
