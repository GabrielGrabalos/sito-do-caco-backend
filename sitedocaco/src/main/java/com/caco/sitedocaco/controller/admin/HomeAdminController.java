package com.caco.sitedocaco.controller.admin;

import com.caco.sitedocaco.dto.request.*;
import com.caco.sitedocaco.dto.response.BannerDTO;
import com.caco.sitedocaco.entity.home.Banner;
import com.caco.sitedocaco.entity.home.News;
import com.caco.sitedocaco.entity.home.Warning;
import com.caco.sitedocaco.service.BannerService;
import com.caco.sitedocaco.service.NewsService;
import com.caco.sitedocaco.service.WarningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class HomeAdminController {

    private final NewsService newsService;
    private final BannerService bannerService;
    private final WarningService warningService;

    // Helper para pegar ID e Role (depende da sua config de security, assumindo Oauth2 padrão)
    private UUID getUserId(Authentication auth) {
        // Exemplo: Se o Principal for customizado ou extraído do Token JWT
        // return UUID.fromString(auth.getName()); // ou lógica específica
        // Para fins deste exemplo, assumimos que o ID venha no Principal
        return UUID.fromString(auth.getName());
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
    }

    // ==================== NEWS (ADMIN + EDITOR) ====================

    @PostMapping("/news")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<News> createNews(@RequestBody @Valid CreateNewsDTO dto, Authentication auth) {
        News created = newsService.createNews(dto, getUserId(auth));
        return ResponseEntity.created(URI.create("/api/public/news/" + created.getSlug())).body(created);
    }

    @PutMapping("/news/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<News> updateNews(@PathVariable UUID id,
                                           @RequestBody @Valid UpdateNewsDTO dto,
                                           Authentication auth) throws AccessDeniedException {
        // Passamos o ID e a flag isAdmin para o service validar a regra de "Editor Owns News"
        return ResponseEntity.ok(newsService.updateNews(id, dto, getUserId(auth), isAdmin(auth)));
    }

    @DeleteMapping("/news/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<Void> deleteNews(@PathVariable UUID id, Authentication auth) throws AccessDeniedException {
        newsService.deleteNews(id, getUserId(auth), isAdmin(auth));
        return ResponseEntity.noContent().build();
    }

    // ==================== BANNERS (ADMIN ONLY) ====================

    @GetMapping("/banners/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BannerDTO>> getActiveBanners() {
        return ResponseEntity.ok(bannerService.getActiveBanners());
    }

    @GetMapping("/banners/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BannerDTO>> getInactiveBanners() {
        return ResponseEntity.ok(bannerService.getInactiveBanners());
    }

    @PostMapping(value = "/banners", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Banner> createBanner(@ModelAttribute @Valid CreateBannerDTO dto) throws IOException {
        return ResponseEntity.ok(bannerService.createBanner(dto));
    }

    @PutMapping(value = "/banners/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Banner> updateBanner(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdateBannerDTO dto) throws IOException {

        Banner updated = bannerService.updateBanner(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/banners/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Banner> toggleBannerActive(@PathVariable UUID id) {
        return ResponseEntity.ok(bannerService.toggleActive(id));
    }

    @PutMapping("/banners/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reorderBanners(@RequestBody @Valid ReorderBannersDTO dto) {
        bannerService.reorderBanners(dto.bannerIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/banners/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBanner(@PathVariable UUID id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== WARNINGS (ADMIN ONLY) ====================
    // Editors geralmente não postam alertas de infra, apenas notícias

    @PostMapping("/warnings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warning> createWarning(@RequestBody @Valid CreateWarningDTO dto) {
        return ResponseEntity.ok(warningService.createWarning(dto));
    }

    @PutMapping("/warnings/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warning> updateWarning(@PathVariable UUID id, @RequestBody @Valid UpdateWarningDTO dto) {
        return ResponseEntity.ok(warningService.updateWarning(id, dto));
    }

    @PatchMapping("/warnings/{id}/expire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> expireWarning(@PathVariable UUID id) {
        warningService.expireWarningNow(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/warnings/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWarning(@PathVariable UUID id) {
        warningService.deleteWarning(id);
        return ResponseEntity.noContent().build();
    }
}