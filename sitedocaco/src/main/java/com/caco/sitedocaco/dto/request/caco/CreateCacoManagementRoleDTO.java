package com.caco.sitedocaco.dto.request.caco;


import com.caco.sitedocaco.entity.caco.CacoManagementRole;
import jakarta.validation.constraints.NotBlank;

public record CreateCacoManagementRoleDTO(
    @NotBlank(message = "O nome do cargo é obrigatório.")
    String name,
) {
}