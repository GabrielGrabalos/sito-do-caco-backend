package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.sticker.CreateStickerDTO;
import com.caco.sitedocaco.dto.response.sticker.StickerAdminDTO;
import com.caco.sitedocaco.dto.response.sticker.StickerPublicDTO;
import com.caco.sitedocaco.entity.event.Event;
import com.caco.sitedocaco.entity.sticker.Sticker;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.EventRepository;
import com.caco.sitedocaco.repository.StickerRepository;
import com.caco.sitedocaco.entity.enums.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;
    private final EventRepository eventRepository;
    private final ImgBBService imgBBService;

    @Transactional
    public StickerAdminDTO createSticker(CreateStickerDTO dto, MultipartFile imageFile) throws IOException {
        if (stickerRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessRuleException("Já existe um sticker com esse nome.");
        }

        if (imageFile == null || imageFile.isEmpty()) {
            throw new BusinessRuleException("Imagem é obrigatória.");
        }

        // Faz upload e valida via ImgBBService (usa ImageType específico de adesivo se existir)
        String imageUrl = imgBBService.uploadImage(imageFile, ImageType.PRODUCT_GALLERY);

        Sticker sticker = new Sticker();
        sticker.setName(dto.name().trim());
        sticker.setDescription(dto.description());
        sticker.setImageUrl(imageUrl);

        if (dto.originEventId() != null) {
            Event event = eventRepository.findById(dto.originEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Evento de origem não encontrado."));
            sticker.setOriginEvent(event);
        }

        Sticker saved = stickerRepository.save(sticker);
        return StickerAdminDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<StickerPublicDTO> listPublic(Pageable pageable) {
        return stickerRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(StickerPublicDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Sticker getStickerEntity(UUID stickerId) {
        return stickerRepository.findById(stickerId)
                .orElseThrow(() -> new ResourceNotFoundException("Sticker não encontrado."));
    }
}
