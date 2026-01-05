package com.caco.sitedocaco.dto.response;

import java.util.List;

public record DashboardDTO(
        List<BannerDTO> banners,
        List<WarningDTO> warnings,
        List<NewsSummaryDTO> latestNews
) {}