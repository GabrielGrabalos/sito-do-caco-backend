package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.sticker.ClaimStickerDTO;
import com.caco.sitedocaco.dto.response.sticker.ClaimStickerResponseDTO;
import com.caco.sitedocaco.dto.response.sticker.MyStickerDTO;
import com.caco.sitedocaco.service.UserStickerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/stickers")
@RequiredArgsConstructor
public class UserStickerController {

    private final UserStickerService userStickerService;

    @PostMapping("/claim")
    public ResponseEntity<ClaimStickerResponseDTO> claim(@RequestBody @Valid ClaimStickerDTO dto) {
        return ResponseEntity.ok(userStickerService.claim(dto.code()));
    }

    @GetMapping
    public ResponseEntity<Page<MyStickerDTO>> myStickers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userStickerService.myStickers(pageable));
    }
}

