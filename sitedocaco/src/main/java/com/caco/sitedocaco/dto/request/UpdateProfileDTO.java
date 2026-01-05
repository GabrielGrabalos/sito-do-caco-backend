package com.caco.sitedocaco.dto.request;

// O usuário só pode atualizar nome e foto (email e role são fixos)
public record UpdateProfileDTO(
        String name,
        String avatarUrl
) {}