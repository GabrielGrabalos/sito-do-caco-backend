package com.caco.sitedocaco.dto.request;

import com.caco.sitedocaco.entity.enums.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleDTO(
        @NotNull(message = "O role não pode ser nulo")
        Role role
) {}

