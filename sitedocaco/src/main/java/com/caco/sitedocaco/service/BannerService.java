package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.CreateBannerDTO;
import com.caco.sitedocaco.dto.response.BannerDTO;
import com.caco.sitedocaco.entity.home.Banner;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public List<BannerDTO> getInactiveBanners() {
        return bannerRepository.findAllByActiveFalseOrderByTitleAsc()
                .stream()
                .map(b -> new BannerDTO(b.getId(), b.getTitle(), b.getImageUrl(), b.getTargetLink()))
                .toList();
    }

    @Transactional
    public Banner createBanner(CreateBannerDTO dto) {
        Banner banner = new Banner();
        banner.setTitle(dto.title());
        banner.setImageUrl(dto.imageUrl());
        banner.setTargetLink(dto.targetLink());
        banner.setActive(dto.active() != null ? dto.active() : true);

        // Regra: Adiciona ao final da lista
        Integer maxOrder = bannerRepository.findMaxDisplayOrder();
        banner.setDisplayOrder(maxOrder == null ? 0 : maxOrder + 1);

        return bannerRepository.save(banner);
    }

    @Transactional
    public Banner toggleActive(UUID id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner não encontrado"));
        banner.setActive(!banner.getActive());
        return bannerRepository.save(banner);
    }

    @Transactional
    public void deleteBanner(UUID id) {
        bannerRepository.deleteById(id);
    }

    /**
     * Função Especial: Reordenar Banners
     * Recebe uma lista de IDs na ordem desejada (A, D, B, C, E)
     * Itera e atualiza o displayOrder de acordo com o índice na lista.
     */
    @Transactional
    public void reorderBanners(List<UUID> orderedIds) {
        // Atualiza a ordem baseada na lista recebida.
        // Nota: Isso funciona mesmo se a lista misturar ativos e inativos,
        // mas idealmente o front deve mandar a lista do contexto que está vendo.
        for (int i = 0; i < orderedIds.size(); i++) {
            UUID id = orderedIds.get(i);
            // Usamos ifPresent para evitar erro caso o ID tenha sido deletado recentemente
            int finalI = i;
            bannerRepository.findById(id).ifPresent(banner -> {
                banner.setDisplayOrder(finalI);
                bannerRepository.save(banner);
            });
        }
    }
}