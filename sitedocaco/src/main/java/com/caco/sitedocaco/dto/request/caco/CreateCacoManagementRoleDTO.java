package com.caco.sitedocaco.dto.request.caco;

import jakarta.validation.constraints.NotBlank;

public record CreateCacoManagementRoleDTO(
    @NotBlank(message = "O nome do cargo é obrigatório.")
    String name
) {
}