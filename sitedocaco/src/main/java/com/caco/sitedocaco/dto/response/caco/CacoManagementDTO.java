package com.caco.sitedocaco.dto.response.caco;

import com.caco.sitedocaco.entity.caco.cacoManagement;

import java.util.UUID;
import java.time.LocalDate;

public record CacoManagementResponseDTO(
    UUID id,
    String name,
    LocalDate startDate;
    LocalDate endDate;

){
    public static CacoManagementResponseDTO fromEntity(CacoMagenement entity){
        return new CacoManagementResponseDTO(
            entity.getId(),
            entity.getname(),
            entity.getstartDate(),
            entity.getendDate()
        );
    }
}