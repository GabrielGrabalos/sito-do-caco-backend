package com.caco.sitedocaco.dto.request.caco;

import java.time.LocalDate;

public record UpdateCacoManagementDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {}
