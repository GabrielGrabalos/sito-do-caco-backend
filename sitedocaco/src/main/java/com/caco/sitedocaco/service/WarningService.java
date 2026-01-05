package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.response.WarningDTO;
import com.caco.sitedocaco.repository.WarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarningService {

    private final WarningRepository warningRepository;

    @Transactional(readOnly = true)
    public List<WarningDTO> getActiveWarnings() {
        return warningRepository.findActiveWarnings(LocalDateTime.now())
                .stream()
                .map(w -> new WarningDTO(w.getId(), w.getMarkdownText(), w.getExpiresAt()))
                .toList();
    }
}