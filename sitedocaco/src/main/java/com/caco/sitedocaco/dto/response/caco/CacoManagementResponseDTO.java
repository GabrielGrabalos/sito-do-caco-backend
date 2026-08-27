package com.caco.sitedocaco.dto.response.caco;

import java.time.LocalDate;
import java.util.UUID;

import com.caco.sitedocaco.entity.caco.CacoManagement;

public record CacoManagementResponseDTO(
    UUID id,
    String name,
    LocalDate startDate,
    LocalDate endDate
) {
    public static CacoManagementResponseDTO fromEntity(CacoManagement entity) {
        return new CacoManagementResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getStartDate(),
            entity.getEndDate()
        );
    }

}