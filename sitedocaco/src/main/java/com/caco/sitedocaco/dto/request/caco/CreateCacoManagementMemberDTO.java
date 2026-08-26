package com.caco.sitedocaco.dto.request.caco;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateCacoManagementMemberDTO(
    @NotNull
    UUID cacoManagementId,

    @NotBlank(message = "O nome é obrigatório")
    String name,

    @NotNull
    UUID roleId
) {}
