package com.caco.sitedocaco.controller.publicController;

import com.caco.sitedocaco.dto.response.BannerDTO;
import com.caco.sitedocaco.dto.response.DashboardDTO;
import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.dto.response.WarningDTO;
import com.caco.sitedocaco.security.ratelimit.RateLimit;
import com.caco.sitedocaco.service.BannerService;
import com.caco.sitedocaco.service.NewsService;
import com.caco.sitedocaco.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/home")
@RequiredArgsConstructor
@RateLimit
public class HomeController {

    private final BannerService bannerService;
    private final WarningService warningService;
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getHomeDashboard() {
        // 1. Busca banners ativos (ordenados por prioridade)
        List<BannerDTO> banners = bannerService.getActiveBanners();

        // 2. Busca avisos urgentes (que não expiraram)
        List<WarningDTO> warnings = warningService.getActiveWarnings();

        // 3. Busca apenas as 3 últimas notícias para a capa
        List<NewsSummaryDTO> latestNews = newsService.getLatestNews(3);

        // 4. Monta o objeto de resposta agregado
        DashboardDTO dashboard = new DashboardDTO(
                banners,
                warnings,
                latestNews
        );

        return ResponseEntity.ok(dashboard);
    }
}