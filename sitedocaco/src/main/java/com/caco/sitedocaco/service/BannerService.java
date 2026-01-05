package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.response.BannerDTO;
import com.caco.sitedocaco.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<BannerDTO> getActiveBanners() {
        return bannerRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(b -> new BannerDTO(b.getId(), b.getTitle(), b.getImageUrl(), b.getTargetLink()))
                .toList();
    }
}