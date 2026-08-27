package com.caco.sitedocaco.dto.response.caco;

import com.caco.sitedocaco.entity.caco.CacoManagementRole;

import java.util.UUID;

public record CacoManagementRoleResponseDTO(
        UUID id,
        String name
) {
    public static CacoManagementRoleResponseDTO fromEntity(CacoManagementRole role) {
        return new CacoManagementRoleResponseDTO(
                role.getId(),
                role.getRoleName()
        );
    }
}
