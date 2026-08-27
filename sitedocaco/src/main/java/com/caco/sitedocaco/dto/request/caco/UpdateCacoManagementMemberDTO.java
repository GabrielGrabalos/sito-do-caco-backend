package com.caco.sitedocaco.dto.request.caco;

import java.util.UUID;

public record UpdateCacoManagementMemberDTO(
        String name,
        UUID roleId
) {}
