package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.sticker.GenerateRedemptionCodesDTO;
import com.caco.sitedocaco.dto.response.sticker.RedemptionCodeBatchResponseDTO;
import com.caco.sitedocaco.entity.sticker.RedemptionCode;
import com.caco.sitedocaco.entity.sticker.Sticker;
import com.caco.sitedocaco.exception.BusinessRuleException;
import com.caco.sitedocaco.repository.RedemptionCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedemptionCodeService {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // sem 0/O/I/1
    private static final int DEFAULT_CODE_LEN = 10;

    private final SecureRandom random = new SecureRandom();

    private final StickerService stickerService;
    private final RedemptionCodeRepository redemptionCodeRepository;

    @Transactional
    public RedemptionCodeBatchResponseDTO generateBatch(UUID stickerId, GenerateRedemptionCodesDTO dto) {
        if (dto.expiresAt() != null && dto.expiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("expiresAt não pode estar no passado.");
        }

        Sticker sticker = stickerService.getStickerEntity(stickerId);

        boolean oneTimeUse = dto.oneTimeUse() == null || dto.oneTimeUse();
        int quantity = dto.quantity();

        // Evita loop infinito se o banco tiver colisões (muito improvável)
        int maxAttempts = quantity * 20;

        Set<String> codes = new HashSet<>(quantity);
        int attempts = 0;
        while (codes.size() < quantity) {
            if (attempts++ > maxAttempts) {
                throw new BusinessRuleException("Não foi possível gerar códigos únicos. Tente novamente.");
            }

            String code = randomCode();
            if (codes.contains(code)) continue;
            if (redemptionCodeRepository.existsById(code)) continue;
            codes.add(code);
        }

        List<RedemptionCode> entities = new ArrayList<>(quantity);
        for (String code : codes) {
            RedemptionCode rc = new RedemptionCode();
            rc.setCode(code);
            rc.setSticker(sticker);
            rc.setIsOneTimeUse(oneTimeUse);
            rc.setIsUsed(false);
            rc.setExpiresAt(dto.expiresAt());
            entities.add(rc);
        }

        redemptionCodeRepository.saveAll(entities);
        return new RedemptionCodeBatchResponseDTO(quantity, new ArrayList<>(codes));
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(RedemptionCodeService.DEFAULT_CODE_LEN);
        for (int i = 0; i < RedemptionCodeService.DEFAULT_CODE_LEN; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}

